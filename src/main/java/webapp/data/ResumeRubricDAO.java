/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.List;
import javax.persistence.EntityManager;
import webapp.model.Resume;
import webapp.model.ResumeRubric;

/**
 *
 * @author msv
 */
public class ResumeRubricDAO {
    public static void mergeFK(EntityManager entityManager, ResumeRubric resumerubric) {
        if (resumerubric != null) {
            JobRubricDAO.merge(entityManager, resumerubric.getJobrubric());
            JobSpecialtyDAO.merge(entityManager, resumerubric.getJobspecialty());
        }    
    }
    
    public static void mergeFK(EntityManager entityManager, List<ResumeRubric> resumerubricList) {
        if (resumerubricList != null) {
            for (ResumeRubric resumerubric : resumerubricList)
                mergeFK(entityManager, resumerubric);
        }    
    }
    
    public static void merge(EntityManager entityManager, Resume resume, List<ResumeRubric> resumerubricList, boolean withFK) {
        if (resumerubricList != null && resumerubricList.size() > 0) {
            for (int i = 0; i < resume.getResumerubricList().size(); i++) {
                ResumeRubric rr = resume.getResumerubricList().get(i);
                if (! resumerubricList.contains(rr)) {
                    entityManager.remove(rr);
                    resume.getResumerubricList().remove(i--);
                }
            }
            
            for (ResumeRubric resumerubric : resumerubricList) {
                if (withFK)
                    mergeFK(entityManager, resumerubric);
                if (! resume.getResumerubricList().contains(resumerubric))
                    resume.getResumerubricList().add(entityManager.merge(resumerubric));
                entityManager.merge(resumerubric);
            }
        } else {
           resume.getResumerubricList().clear();
           deleteByResumeId(entityManager, resume.getId());
        }     
    }
    
    public static int deleteByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("DELETE FROM ResumeRubric rr WHERE resume_id = :resume_id")
                .setParameter("resume_id", resume_id)
                .executeUpdate();
    }
}
