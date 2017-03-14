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
import webapp.model.Institution;
import webapp.model.Resume;

/**
 *
 * @author msv
 */
public class InstitutionDAO {
    public static void mergeFK(EntityManager entityManager, Institution institution) {
        if (institution != null)
            institution.setCity(CityDAO.merge(entityManager, institution.getCity()));
    }
    
    public static void mergeFK(EntityManager entityManager, List<Institution> institutionList) {
        if (institutionList != null) {
            for (Institution institution : institutionList)
                mergeFK(entityManager, institution);
        }
    }
    
    public static void merge(EntityManager entityManager, Resume resume, List<Institution> institutionList, boolean withFK) {
        if (institutionList != null || institutionList.size() > 0) {
            //resume.setInstitutionList(getByResumeId(entityManager, resume.getId()));
            for (int i = 0; i < resume.getInstitutionList().size(); i++) {
                Institution institution = resume.getInstitutionList().get(i);
                if (! institutionList.contains(institution)) {
                    entityManager.remove(institution);
                    resume.getInstitutionList().remove(i--);
                }
            }
            for (Institution institution : institutionList) {
                if (withFK)
                    mergeFK(entityManager, institution);
                int pos = resume.getInstitutionList().indexOf(institution);
                if (pos == -1) 
                    entityManager.merge(institution);
                else {
                    Institution institution2 = resume.getInstitutionList().get(pos);
                    if (! (institution2.getDescription() == null && institution.getDescription() == null) && 
                            ! Objects.equals(institution2.getDescription(), institution.getDescription()))
                        institution2.setDescription(institution.getDescription());
                    if (! (institution2.getFrom_day() == null && institution.getFrom_day() == null) && (
                            (institution2.getFrom_day() == null && institution.getFrom_day() != null) ||
                         (institution2.getFrom_day() != null && institution.getFrom_day() == null) ||
                          (institution2.getFrom_day().equals(institution.getFrom_day()))
                            ))
                        institution2.setFrom_day(institution.getFrom_day());                    
                    if (! (institution2.getTo_day() == null && institution.getTo_day() == null) && (
                            (institution2.getTo_day() == null && institution.getTo_day() != null) ||
                         (institution2.getTo_day() != null && institution.getTo_day() == null) ||
                          (institution2.getTo_day().equals(institution.getTo_day()))
                            ))
                        institution2.setTo_day(institution.getTo_day());
                }
            }
        } else {
           resume.getInstitutionList().clear();
           deleteByResumeId(entityManager, resume.getId());
        } 
    }
    
    public static int deleteByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("DELETE FROM Institution i WHERE resume_id = :resume_id")
                .setParameter("resume_id", resume_id)
                .executeUpdate();
    }
    
    /*
    public static List<Institution> getByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("SELECT i FROM Institution i WHERE resume_id = :resume_id ORDER BY i.from_day")
                .setParameter("resume_id", resume_id)
                .getResultList();
    }
    */
}
