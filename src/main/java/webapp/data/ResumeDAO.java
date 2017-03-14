/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import webapp.model.CitiesReference;
import webapp.model.CitiesReferenceComparator;
import webapp.model.Institution;
import webapp.model.Resume;
import webapp.model.ResumeJob;
import webapp.model.ResumeRubric;
import webapp.model.ResumeRubricComparator;
import webapp.utils.StaticVar;

/**
 *
 * @author msv
 */
public class ResumeDAO {
    
    public static void clear() {
        CityDAO.clear();
        CurrencyDAO.clear();
        JobRubricDAO.clear();
        JobSpecialtyDAO.clear();
    }
    
    public static void mergeFK(EntityManager entityManager, Resume resume) {
        resume.setCity(CityDAO.merge(entityManager, resume.getCity()));
        resume.setCurrency(CurrencyDAO.merge(entityManager, resume.getCurrency()));
        resume.setCitydistinct(CityDistinctDAO.merge(entityManager, resume.getCitydistinct()));
        resume.setSchedule(ScheduleDAO.merge(entityManager, resume.getSchedule()));
        resume.setWorkingtype(WorkingTypeDAO.merge(entityManager, resume.getWorkingtype()));
        resume.setEducation(EducationDAO.merge(entityManager, resume.getEducation()));
        resume.setExperience(ExperienceDAO.merge(entityManager, resume.getExperience()));
    }
    
    public static void mergeFKonList(EntityManager entityManager, Resume resume) {
        CitiesReferenceDAO.mergeFK(entityManager, resume.getCitiesreferenceList());
        InstitutionDAO.mergeFK(entityManager, resume.getInstitutionList());
        ResumeRubricDAO.mergeFK(entityManager, resume.getResumerubricList());
        ResumeJobDAO.mergeFK(entityManager, resume.getResumejobList());
    }
    
    public static void mergeFKAll(EntityManager entityManager, Resume resume) {
        mergeFK(entityManager, resume);
        mergeFKonList(entityManager, resume);
    }
    
    public static Resume merge(EntityManager entityManager, Resume resume, boolean withFK) {
        Resume resume_merged = entityManager.merge(resume);
        
        initLazyCollections(entityManager, resume_merged);
        
        CitiesReferenceDAO.merge(entityManager, resume_merged, resume.getCitiesreferenceList(), withFK);
        InstitutionDAO.merge(entityManager, resume_merged, resume.getInstitutionList(), withFK);
        ResumeRubricDAO.merge(entityManager, resume_merged, resume.getResumerubricList(), withFK);
        ResumeJobDAO.merge(entityManager, resume_merged, resume.getResumejobList(), withFK);
        
        return resume_merged;
    }
    
    public static Resume merge(EntityManager entityManager, Resume resume) {
        return merge(entityManager, resume, false);        
    }
    
    public static Resume mergeWithFK(EntityManager entityManager, Resume resume) {
        mergeFK(entityManager, resume);
        return merge(entityManager, resume, true);
    }
    
    public static void initLazyFileds(EntityManager entityManager, Resume resume) {
        resume.getPersonal_qualities();
        resume.getSkills();
    }
    
    public static void initLazyCollections(EntityManager entityManager, Resume resume) {
        //resume.getCitiesreferenceList().size();
        //Collections.sort(resume.getCitiesreferenceList(), new CitiesReferenceComparator());
        resume.setCitiesreferenceList(getCitiesReferencesOrdered(entityManager, resume.getId()));
        
        //resume.getInstitutionList().size();
        resume.setInstitutionList(getInstitutionsOrderedByFromDate(entityManager, resume.getId()));

        //resume.getResumejobList().size();
        resume.setResumejobList(getResumeJobsOrderedByFromDateDesc(entityManager, resume.getId()));
        
        //resume.getResumerubricList().size();
        //Collections.sort(resume.getResumerubricList(), new ResumeRubricComparator());
        resume.setResumerubricList(getResumeRubricsOrdered(entityManager, resume.getId()));
    }
    
    public static Resume findResume(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.find(Resume.class, resumeId);
    }
    
    public static Resume findResumeFull(EntityManager entityManager, Long resumeId) {
        Resume resume = findResume(entityManager, resumeId);
        if (resume != null) {
            initLazyFileds(entityManager, resume);
            initLazyCollections(entityManager, resume);
        }
        return resume;
    }
    
    public static List<Resume> getResumesByFilter(EntityManager entityManager, Integer firstResult, Integer maxResults , Integer jobrubricId, String cityTitle, String resumeHeaderSubstr) {
/* 
Postgres SQL example
SELECT * FROM resume r WHERE 
r.id IN (
	SELECT cr.resume_id FROM cities_reference cr, city c, resume_rubrics rr 
	 WHERE cr.city_id = c.id AND c.title = 'Екатеринбург'
	   AND cr.resume_id = rr.resume_id AND rr.jobrubric_id = 1
)
AND LOWER(r.header) LIKE '%бухгалтер%'
ORDER BY r.header

SELECT * FROM resume r WHERE 
EXISTS (
	SELECT cr.resume_id FROM cities_reference cr, city c, resume_rubrics rr 
	 WHERE r.id = cr.resume_id
       AND cr.city_id = c.id AND c.title = 'Екатеринбург'
	   AND cr.resume_id = rr.resume_id AND rr.jobrubric_id = 1
)
AND LOWER(r.header) LIKE '%бухгалтер%'
ORDER BY r.header
        
JPQL
SELECT r FROM Resume r
 WHERE 
EXISTS (
    SELECT rr.resume FROM CitiesReference cr, ResumeRubrics rr    
     WHERE r.id = rr.resume.id
       AND rr.jobrubric.id = 1
       AND r.id = cr.resume.id
       AND cr.city.title = 'Екатеринбург'
)     
AND LOWER(r.header) LIKE '%бухгалтер%'
ORDER BY r.header        
*/   

        StringBuilder queryBuild = new StringBuilder("SELECT r FROM Resume r");
        StringBuilder filterBuild = new StringBuilder();

        boolean filtered = false;
        if (cityTitle != null && ! cityTitle.equals("") && ! cityTitle.equals(StaticVar.allStr)) {
            queryBuild.append(" WHERE EXISTS ( SELECT");
            queryBuild.append(" cr.resume FROM CitiesReference cr");
            filterBuild.append(" WHERE r.id = cr.resume.id AND cr.city.title = :city");
            filtered = true;
        }
        if (jobrubricId != null &&  ! jobrubricId.equals(0)) {
            if (filtered) {
                queryBuild.append(", ResumeRubric rr");
                filterBuild.append(" AND");
            } else {
                queryBuild.append(" WHERE EXISTS ( SELECT");
                queryBuild.append(" rr.resume FROM ResumeRubric rr");
                filterBuild.append(" WHERE");
            }
            filterBuild.append(" r.id = rr.resume.id AND rr.jobrubric.id = :jobrubricid"); 
            //queryBuild.append(" EXISTS (SELECT 1 FROM ResumeRubric rr WHERE r.id = rr.PK.resume.id AND rr.PK.job_rubric.id = ?2)");
            filtered = true;
        }
        if (filtered) {
            filterBuild.append(")");
        }
        if (resumeHeaderSubstr != null && ! resumeHeaderSubstr.equals("")) {
            if (filtered)
                filterBuild.append(" AND");
            else
                filterBuild.append(" WHERE");
            filterBuild.append(" LOWER(r.header) LIKE :header");
            filtered = true;
        }
        filterBuild.append(" ORDER BY r.header");
 
        String queryStr = queryBuild.toString() + filterBuild.toString();
        Query q = entityManager.createQuery(queryStr);
        if (cityTitle != null && ! cityTitle.equals("") && ! cityTitle.equals(StaticVar.allStr))
            q.setParameter("city", cityTitle);
        if (jobrubricId != null &&  ! jobrubricId.equals(0)) 
            q.setParameter("jobrubricid", jobrubricId);
        if (resumeHeaderSubstr != null && ! resumeHeaderSubstr.equals("")) 
            q.setParameter("header", resumeHeaderSubstr);
        
        if (firstResult != null)
            q.setFirstResult(firstResult);
        if (maxResults != null)
            q.setMaxResults(maxResults);
        
        return q.getResultList();
    }
    
    /*
    public static Resume findDetail(EntityManager entityManager, Long id) {
        EntityGraph<Resume> eg = entityManager.createEntityGraph(Resume.class);
        
        Subgraph<CitiesReference> egCitiesReference = eg.addSubgraph("citiesreferenceList");
        egCitiesReference.addAttributeNodes("citydistinct");
        
        Subgraph<Institution> egInstitution = eg.addSubgraph("institutionList");

        Subgraph<ResumeJob> egResumeJob = eg.addSubgraph("resumejobList");
        egResumeJob.addAttributeNodes("city");
        
        Subgraph<ResumeRubric> egResumeRubric = eg.addSubgraph("resumerubricsList");
        egResumeRubric.addAttributeNodes("jobrubric");
        egResumeRubric.addAttributeNodes("jobspecialty");
        
        Map<String, Object> hints = new HashMap<String, Object>();
        hints.put("javax.persistence.loadgraph", eg);

        return entityManager.find(Resume.class, id, hints);
    }
    */
    
    public static List<CitiesReference> getCitiesReferences(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT cr FROM CitiesReference cr WHERE resume.id = ?1")
                .setParameter(1, resumeId)
               .getResultList();
    }
    
    public static List<CitiesReference> getCitiesReferencesOrdered(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT cr FROM CitiesReference cr WHERE resume.id = ?1 ORDER BY cr.city.title, cr.citydistinct.title")
                .setParameter(1, resumeId)
               .getResultList();
    }
    
    public static List<ResumeRubric> getResumeRubrics(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT rr FROM ResumeRubric rr WHERE resume.id = ?1")
                .setParameter(1, resumeId)
                .getResultList();
    }
    
    public static List<ResumeRubric> getResumeRubricsOrdered(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT rr FROM ResumeRubric rr WHERE resume.id = ?1 ORDER BY rr.jobrubric.title, rr.jobspecialty.title")
                .setParameter(1, resumeId)
                .getResultList();
    }
    
    public static List<ResumeJob> getResumeJobs(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT rj FROM ResumeJob rj WHERE resume.id = ?1")
                .setParameter(1, resumeId)
                .getResultList();
    }
    
    public static List<ResumeJob> getResumeJobsOrderedByFromDateDesc(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT rj FROM ResumeJob rj WHERE resume.id = ?1 ORDER BY rj.PK.from_day DESC")
                .setParameter(1, resumeId)
                .getResultList();
    }
    public static List<Institution> getInstitutions(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT i FROM Institution i WHERE resume.id = ?1")
                .setParameter(1, resumeId)
                .getResultList();
    }
    
    public static List<Institution> getInstitutionsOrderedByFromDate(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT i FROM Institution i WHERE resume.id = ?1 ORDER BY i.from_day")
                .setParameter(1, resumeId)
                .getResultList();
    }
    
    public static List<Institution> getMainInstitutions(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT i FROM Institution i WHERE resume.id = ?1 AND additional = ?2")
                .setParameter(1, resumeId)
                .setParameter(2, false)
                .getResultList();
    }
    
    public static List<Institution> getAdditionalInstitutions(EntityManager entityManager, @NotNull Long resumeId) {
        return entityManager.createQuery("SELECT i FROM Institution i WHERE resume.id = ?1 AND additional = ?2")
                .setParameter(1, resumeId)
                .setParameter(2, true)
                .getResultList();
    }
    
    public static List<Resume> getResumesByOwner(EntityManager entityManager, Long ownerId, Long currentResumeId) {
        if (ownerId == null)
            return null;
        StringBuilder sb = new StringBuilder("SELECT r FROM Resume r WHERE r.owner_id = ?1");
        if (currentResumeId != null)
            sb.append(" AND r.id <> ?2");
        sb.append(" ORDER BY r.header");
        Query q = entityManager.createQuery(sb.toString());
        q.setParameter(1, ownerId);
        if (currentResumeId != null)
            q.setParameter(2, currentResumeId);
        return q.getResultList();
    }
}
