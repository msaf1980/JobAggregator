/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.validation.constraints.NotNull;
import webapp.model.City;

/**
 *
 * @author msv
 */
public class CityDAO {
    
    private static HashSet<Integer> cityIds = new HashSet<Integer>();
    
    public static boolean contains(Integer cityId) {
        return cityIds.contains(cityId);
    }
    
    public static void add(Integer cityId) {
        cityIds.add(cityId);
    }
    
    public static void clear() {
        cityIds.clear();
    }
    
    public static City merge(EntityManager entityManager, City city) {
        if (city == null || city.getTitle() == null) 
            return null;
        //if (! entityManager.contains(city))
        if (city.getId() == null) {
            List<City> cityByTitle = getByTitle(entityManager, city.getTitle());
            if (cityByTitle.size() == 0)
                return null;
            else {
                city.setId(cityByTitle.get(0).getId());
                return cityByTitle.get(0);
            }
        } else {
            if (contains(city.getId()))
                return city;
            /*
            City city_in_db = find(entityManager, city.getId());
            if (city_in_db == null) {
                entityManager.persist(city);
                return city;
            } else {
                if (! Objects.equals(city.getTitle(), city_in_db.getTitle()))
                    city_in_db.setTitle(city.getTitle());    
                return city_in_db;
            }
            */
            add(city.getId());
            return entityManager.merge(city);
        }
    }
     
    public static City find(EntityManager entityManager, @NotNull Integer id) {
        return entityManager.find(City.class, id);
    }
    
    public static List<City> getByTitle(EntityManager entityManager, String title) {
        return entityManager.createQuery("SELECT c FROM City c WHERE title = ?1")
                .setParameter(1, title)
                    .getResultList();
    }
    
    public static List<City> getAllOrdered(EntityManager entityManager) {
        return entityManager.createQuery("SELECT c FROM City c ORDER BY c.title")
                    .getResultList();
    }
    
    public static List<City> getAllOrderedWithResumes(EntityManager entityManager) {
        return entityManager.createQuery("SELECT c FROM City c WHERE EXISTS (SELECT 1 FROM CitiesReference cr WHERE cr.PK.city_id = c.id) ORDER BY c.title")
                    .getResultList();
                        
        /*
        return entityManager.createNativeQuery("SELECT c.id, c.title FROM city c WHERE c.id IN (SELECT DISTINCT cr.cityid FROM cities_reference cr) ORDER BY c.title",
                                                     "CityMapping")
                    .getResultList();
        */
    }
}
