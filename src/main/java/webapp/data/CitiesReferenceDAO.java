/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.List;
import java.util.SortedSet;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import webapp.model.CitiesReference;
import webapp.model.City;
import webapp.model.CityDistinct;
import webapp.model.Resume;

/**
 *
 * @author msv
 */

public class CitiesReferenceDAO {
    
    public static void mergeFK(EntityManager entityManager, CitiesReference citiesreference) {
        if (citiesreference != null) {
            citiesreference.setCity(CityDAO.merge(entityManager, citiesreference.getCity()));
            citiesreference.setCitydistinct(CityDistinctDAO.merge(entityManager, citiesreference.getCitydistinct()));
        }    
    }
    
    public static void mergeFK(EntityManager entityManager, List<CitiesReference> citiesreferenceList) {
        if (citiesreferenceList != null) {
            for (CitiesReference citiesreference : citiesreferenceList)
                mergeFK(entityManager, citiesreference);
        }    
    }
    
    public static void merge(EntityManager entityManager, Resume resume, List<CitiesReference> citiesreferenceList, boolean withFK) {
        if (citiesreferenceList != null && citiesreferenceList.size() > 0) {
            //resume.setCitiesreferenceList(getByResumeId(entityManager, resume.getId()));
            for (int i = 0; i < resume.getCitiesreferenceList().size(); i++) {
                CitiesReference cr = resume.getCitiesreferenceList().get(i);
                if (! citiesreferenceList.contains(cr)) {
                    entityManager.remove(cr);
                    resume.getCitiesreferenceList().remove(i--);
                }
            }
            for (CitiesReference citiesreference : citiesreferenceList) {
                if (withFK)
                    mergeFK(entityManager, citiesreference);
                if (! resume.getCitiesreferenceList().contains(citiesreference))
                    resume.getCitiesreferenceList().add(entityManager.merge(citiesreference));
            }
        } else {
           resume.getCitiesreferenceList().clear();
           deleteByResumeId(entityManager, resume.getId());
        }    
    }

    public static int deleteByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("DELETE FROM CitiesReference cr WHERE resume_id = :resume_id")
                .setParameter("resume_id", resume_id)
                .executeUpdate();
    }
   
    /*
    public static List<CitiesReference> getByResumeId(EntityManager entityManager, Long resume_id) {
        return entityManager.createQuery("SELECT cr FROM CitiesReference cr WHERE resume_id = :resume_id ORDER BY cr.city.title, cr.citydistinct.title")
                .setParameter("resume_id", resume_id)
                .getResultList();
    }
    */
}
