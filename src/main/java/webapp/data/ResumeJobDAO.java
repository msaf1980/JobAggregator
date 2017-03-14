/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import static webapp.data.CitiesReferenceDAO.deleteByResumeId;
import webapp.model.Resume;
import webapp.model.ResumeJob;

/**
 *
 * @author msv
 */
public class ResumeJobDAO {
    public static void mergeFK(EntityManager entityManager, ResumeJob resumejob) {
        if (resumejob != null)
            resumejob.setCity(CityDAO.merge(entityManager, resumejob.getCity()));
    }
    
    public static void mergeFK(EntityManager entityManager, List<ResumeJob> resumejobList) {
        if (resumejobList != null) {
            for (ResumeJob resumejob : resumejobList)
                mergeFK(entityManager, resumejob);
        }    
    }
    
    public static void merge(EntityManager entityManager, Resume resume, List<ResumeJob> resumejobList, boolean withFK) {
        if (resumejobList != null && resumejobList.size() > 0) {
            for (int i = 0; i < resume.getResumejobList().size(); i++) {
                ResumeJob rj = resume.getResumejobList().get(i);
                if (! resumejobList.contains(rj)) {
                    entityManager.remove(rj);
                    resume.getResumejobList().remove(i--);
                }
            }
            for (ResumeJob resumejob : resumejobList) {
                if (withFK)
                   mergeFK(entityManager, resumejob);
                int pos = resume.getResumejobList().indexOf(resumejob);
                if (pos == -1) 
                    entityManager.merge(resumejob);
                else {
                    ResumeJob resumejob2 = resume.getResumejobList().get(pos);
                    if (! (resumejob2.getCity() == null && resumejob.getCity() == null) && 
                            ! Objects.equals(resumejob2.getCity(), resumejob.getCity()))
                        resumejob2.setCity(resumejob.getCity());
                    if (! (resumejob2.getDescription() == null && resumejob.getDescription() == null) && 
                            ! Objects.equals(resumejob2.getDescription(), resumejob.getDescription()))
                        resumejob2.setDescription(resumejob.getDescription());
                    if (! (resumejob2.getTo_day() == null && resumejob.getTo_day() == null) && (
                            (resumejob2.getTo_day() == null && resumejob.getTo_day() != null) ||
                         (resumejob2.getTo_day() != null && resumejob.getTo_day() == null) ||
                          (resumejob2.getTo_day().equals(resumejob.getTo_day()))
                            ))
                        resumejob2.setTo_day(resumejob.getTo_day());
                }
            }            
        } else {
           resume.getResumejobList().clear();
           deleteByResumeId(entityManager, resume.getId());
        }
    }
    
    public static int deleteByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("DELETE FROM ResumeJob rj WHERE resume_id = :resume_id")
                .setParameter("resume_id", resume_id)
                .executeUpdate();
    }
}
