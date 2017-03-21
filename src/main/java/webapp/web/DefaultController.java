/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.web;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import webapp.data.CityDAO;
import webapp.data.EntityManagerHelper;
import webapp.data.JobRubricDAO;
import webapp.data.ResumeDAO;

import webapp.data.Resumes;

import webapp.forms.ResumesSearch;
import webapp.forms.ResumesSearchValidator;

import webapp.data.WebExtractorE1;
import webapp.forms.Resync;
import webapp.model.CitiesReference;
import webapp.model.City;
import webapp.model.IdPage;
import webapp.model.Institution;
import webapp.model.JobRubric;
import webapp.model.Resume;
import webapp.model.ResumeRubric;
import webapp.utils.StaticVar;

@Controller @SessionAttributes("resumesSearch")
public class DefaultController {

    //@Inject
    //EntityManager em;
    EntityManagerFactory managerFactory = null;
    String connection_error;
    //boolean debug = false;
    boolean debug = true;
    
    public DefaultController() {

    }
    /**
    * default URL mapping
    */
    @RequestMapping(value = {"/", "/index" }, method = RequestMethod.GET)
    public String index(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch) {
        initResumesSearchForm(model, resumesSearch);
        //return page (index.jsp)
        return "index";
    }
    
    /**
     * init locations combobox
     * @param entityManager
     * @param model 
     */
    public void initLocations(EntityManager entityManager, Model model) {
        //ComboBox
        List<String> locations = new ArrayList<>();
        locations.add(StaticVar.allStr);
        List<City> cities = CityDAO.getAllOrderedWithResumes(entityManager);
        for (City c : cities) {
            locations.add(c.getTitle());
        }
        model.addAttribute("locations", locations);    
    }
    
    /**
     * Search Form init
     * @param model
     * @param resumesSearch 
     */
    @ModelAttribute("resumesSearch")
    private void initResumesSearchForm(Model model, ResumesSearch resumesSearch) {
        //Form name - like form commandName in jsp page
        if (resumesSearch == null) {
            resumesSearch = new ResumesSearch();
        }
        
        model.addAttribute("resumesSearch", resumesSearch);
        
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            initLocations(entityManager, model);
            
            /*
            //Listbox
            List<String> rubrics = new ArrayList<>();
            List<JobRubric> jobRubrics = JobRubricDAO.getAllOrdered(entityManager);
            for (JobRubric jr : jobRubrics) {
                rubrics.add(jr.getTitle());
            }
            model.addAttribute("rubrics", rubrics);
             */
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }

    //-------------------------    
    @RequestMapping(value = {"/", "/index" }, method = RequestMethod.POST, params = "submit")
    public String submitResumesForm(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch, BindingResult result) {
        //jsp page name
        String returnVal;

        ResumesSearchValidator resumesSearchValidator = new ResumesSearchValidator();
        resumesSearchValidator.validate(resumesSearch, result);
        if (result.hasErrors()) {
            //model.addAttribute(BindingResult.class.getName() + ".vacanciesSearch", result);
            //return to default page
            initResumesSearchForm(model, resumesSearch);
            return "index";
        } else {
            //Mark Session Complete
            //status.setComplete();
            //add params from form
            initResumesSearchForm(model, resumesSearch);
            return "index";

        }
    }
    //-------------------------
    @RequestMapping(value = "/", method = RequestMethod.POST, params = "resync")
    public String submitResyncForm(Model model, @ModelAttribute("resync") Resync resync) {
        String status;
        try {
            status = resync();
            resync.setMessage(status);
        } catch (IOException e) {
            resync.setMessage(e.getMessage());
        }
        return "resync";
    }

    //-------------------------
    public void lock(EntityManager entityManager) {
        String lockSQL = "LOCK TABLE resume IN EXCLUSIVE MODE NOWAIT";
        try {
            Query q = entityManager.createNativeQuery(lockSQL);  
            q.executeUpdate();
        } catch (RuntimeException e) {
            throw new RuntimeException("Couldn't lock table for resync");
        }
    }
    //-------------------------
    public String resync() throws IOException {
        final int batch = 100;
        final int fetch = 50;
        long count = 0;
        long merged = 0;
        long deleted = 0;
        
        long start = System.currentTimeMillis();
                
        HashSet<Long> resumeIds = new HashSet<Long>();
        
        //HashSet<Integer> cityIds = new HashSet<Integer>();
        Resumes resumes;
        WebExtractorE1 we = new WebExtractorE1(fetch);
        int n = 0;
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        EntityTransaction tx = null;
        ResumeDAO.clear();
        try {
            entityManager.setFlushMode(FlushModeType.COMMIT);
            tx = entityManager.getTransaction();
            tx.begin();
            
            lock(entityManager);
            while ((resumes = we.getResumes(debug)) != null) {
                int size = resumes.getResumes().size();
                if (size > 0) {
                    count += size;
                    
                    for (int i = 0; i < size; i++) {
                        Resume resume_new = resumes.getResumes().get(i);
                        
                        if (resumeIds.contains(resume_new.getId()))
                           continue; 
                        
                        resumeIds.add(resume_new.getId());
                        
                        ResumeDAO.mergeWithFK(entityManager, resume_new);
                        merged++;
                        n++;
                        if (n >= batch) {
                            n = 0;
                            entityManager.flush();
                            entityManager.clear();
                        }
                    }
                }
            }
            entityManager.flush();
            entityManager.clear();
            //tx.commit();
            if (count > 0) {
                ArrayList<Long> resumeDeletedIds = new ArrayList<Long>();
                ArrayList<Integer> cityDeletedIds = new ArrayList<Integer>();
                StatelessSession session = null;
                ScrollableResults results = null;
                try {
                    //Get Hibernate session - no batch fetch in JPA
                    session = ((Session) entityManager.getDelegate()).getSessionFactory().openStatelessSession();
                    results = session.createSQLQuery("SELECT r.id FROM resume r WHERE r.mod_day < :mod_day")
                            .setFetchSize(batch).setCacheable(false)
                            .setDate("mod_day", new Date(new Date().getTime() - 7*24*60*60*1000))
                                .scroll(ScrollMode.FORWARD_ONLY);
                        while (results.next()) {
                            Object[] row = results.get();
                            Long id = ((BigInteger) row[0]).longValue();
                            if (! resumeIds.contains(id))
                                resumeDeletedIds.add(id);
                        }
                } finally {
                    try {
                        if (results != null)
                            results.close();
                    } finally { }
                    try {
                        if (session != null)
                            session.close();
                    } finally { }
                }
       
                n = 0;
                for (Long id : resumeDeletedIds) {
                    entityManager.createNativeQuery("DELETE FROM resume_jobs r WHERE r.resume_id = :resume_id")
                            .setParameter("resume_id", id)
                            .executeUpdate();
                    entityManager.createNativeQuery("DELETE FROM resume_rubrics r WHERE r.resume_id = :resume_id")
                            .setParameter("resume_id", id)
                            .executeUpdate();
                    entityManager.createNativeQuery("DELETE FROM institution i WHERE i.resume_id = :resume_id")
                            .setParameter("resume_id", id)
                            .executeUpdate();
                    entityManager.createNativeQuery("DELETE FROM cities_reference i WHERE i.resume_id = :resume_id")
                            .setParameter("resume_id", id)
                            .executeUpdate();
                    n += entityManager.createNativeQuery("DELETE FROM resume r WHERE r.id = :resume_id")
                            .setParameter("resume_id", id)
                            .executeUpdate();
                    if (n >= batch) {
                        deleted += n;
                        n = 0;
                        entityManager.flush();
                    }
                }
            }
            entityManager.flush();
            entityManager.clear();
            tx.commit();
        } finally {
            ResumeDAO.clear();
            if (tx != null && tx.isActive())
                tx.rollback();
            EntityManagerHelper.closeEntityManager();
        }
        long end = System.currentTimeMillis();
        return "Resumes: merged " + merged + ", deleted " + deleted + ". Total time " + (end - start) + " msec.";
    }

    /**
     * init Job Rubrics
     * @param entityManager
     * @param model 
     */
    public void initRubrics(EntityManager entityManager, Model model) {
        List<JobRubric> jobRubricList = JobRubricDAO.getAllOrdered(entityManager);
        model.addAttribute("jobRubricList", jobRubricList);    
    }
    
    /**
     * init Job Rubrics Form
     * @param model
     * @param resumesSearch 
     */
    @ModelAttribute("resumesSearch")
    private void initRubricsForm(Model model, ResumesSearch resumesSearch) {
        //Form name - like form commandName in jsp page
        if (resumesSearch == null) {
            resumesSearch = new ResumesSearch();
        }
        
        model.addAttribute("resumesSearch", resumesSearch);
        
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            initLocations(entityManager, model);
            initRubrics(entityManager, model);
            
            /*
            //Listbox
            List<String> rubrics = new ArrayList<>();
            List<JobRubric> jobRubrics = JobRubricDAO.getAllOrdered(entityManager);
            for (JobRubric jr : jobRubrics) {
                rubrics.add(jr.getTitle());
            }
            model.addAttribute("rubrics", rubrics);
             */
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
    
    /**
     * /rubrics mapping - set filter
     * @param model
     * @param resumesSearch
     * @param result
     * @return 
     */
    @RequestMapping(value = {"/rubrics" }, method = RequestMethod.POST, params = "submit")
    public String rubricsSubmitResumesForm(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch, BindingResult result) {
        //jsp page name
        String returnVal;

        ResumesSearchValidator resumesSearchValidator = new ResumesSearchValidator();
        resumesSearchValidator.validate(resumesSearch, result);
        if (result.hasErrors()) {
            //model.addAttribute(BindingResult.class.getName() + ".vacanciesSearch", result);
            //return to default page
            initRubricsForm(model, resumesSearch);
            return "rubrics";
        } else {
            //Mark Session Complete
            //status.setComplete();
            //add params from form
            initRubricsForm(model, resumesSearch);
            return "rubrics";

        }
    }
    
    /**
     * /rubrics mapping
     * rubric list
     * @param model
     * @return 
     */
    @RequestMapping(value = "/rubrics", method = RequestMethod.GET)
    public String rubrics(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch) {
        initRubricsForm(model, resumesSearch);
        return "rubrics";
    }
    
    /**
     * /rubrics/id/{rubricIdStr}/{pageIdStr} mapping
     * Display resumes in rubric
     * @param model
     * @param resumesSearch
     * @param rubricIdStr
     * @param pageIdStr
     * @return 
     */
    @RequestMapping(value = "/rubrics/id/{rubricIdStr}/{pageIdStr}", method = RequestMethod.GET)
    public String rubricResumes(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch,
            @PathVariable String rubricIdStr, @PathVariable String pageIdStr) {
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            int resumesPerPage = 20;
            int pageId = Integer.parseInt(pageIdStr);
            List<Resume> resumeList = ResumeDAO.getResumesByFilter(entityManager, resumesPerPage * pageId, resumesPerPage, 
                    Integer.parseInt(rubricIdStr), resumesSearch.getLocation(), resumesSearch.getSearchstr());
            model.addAttribute("resumeList", resumeList);
            List<IdPage> resumePageList = new ArrayList<IdPage>();
            int fPage = pageId - 5;
            int lPage = pageId;
            if (fPage < 0)
                fPage = 0;
            else if (fPage > 1) {
                resumePageList.add(new IdPage(new Integer(0).toString(), new Integer(1).toString()));
                resumePageList.add(new IdPage("", "..")); 
            }
            for (int i = fPage; i <= lPage; i++) {
                if (i == lPage && resumeList.isEmpty())
                    continue;
                if (i == pageId) 
                    resumePageList.add(new IdPage("", new Integer(i + 1).toString()));
                else
                    resumePageList.add(new IdPage(new Integer(i).toString(), new Integer(i + 1).toString()));
            }
            if (resumeList.size() > 0)
                resumePageList.add(new IdPage(new Integer(pageId + 1).toString(), ">"));
            model.addAttribute("resumePageList", resumePageList);
            
            model.addAttribute("locationId", rubricIdStr);
                    
            return "resumesinrubric";
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
    
    /**
     * /resume/id/{resumeIdStr} mapping
     * Display resume details
     * @param model
     * @param resumesSearch
     * @param resumeIdStr
     * @return 
     */
    @RequestMapping(value = "/resume/id/{resumeIdStr}", method = RequestMethod.GET)
    public String resume(Model model, @ModelAttribute("resumesSearch") ResumesSearch resumesSearch,
            @PathVariable String resumeIdStr) {
        EntityManager entityManager = EntityManagerHelper.getEntityManager();
        try {
            Resume resume = ResumeDAO.findResumeFull(entityManager, Long.parseLong(resumeIdStr));
            model.addAttribute("resume", resume);
            if (resume != null) {
                List<Resume> otherResumes = ResumeDAO.getResumesByOwner(entityManager, resume.getOwner_id(), resume.getId());
                model.addAttribute("otherResumes", otherResumes);
            }
            return "resume";
        } finally {
            EntityManagerHelper.closeEntityManager();
        }
    }
}
