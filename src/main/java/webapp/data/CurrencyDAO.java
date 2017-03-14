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
import webapp.model.Currency;

/**
 *
 * @author msv
 */
public class CurrencyDAO {
    
    private static HashSet<Integer> currencyIds = new HashSet<Integer>();
    
    public static boolean contains(Integer cityId) {
        return currencyIds.contains(cityId);
    }
    
    public static void add(Integer cityId) {
        currencyIds.add(cityId);
    }
    
    public static void clear() {
        currencyIds.clear();
    }
    
    public static Currency merge(EntityManager entityManager, Currency currency) {
        if (currency == null || currency.getTitle() == null) 
            return null;
        if (contains(currency.getId()))
            return currency;
        add(currency.getId());
        return entityManager.merge(currency);
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
