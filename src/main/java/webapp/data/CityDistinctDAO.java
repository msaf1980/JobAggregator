/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import webapp.model.City;
import webapp.model.CityDistinct;

/**
 *
 * @author msv
 */
public class CityDistinctDAO {
    public static CityDistinct merge(EntityManager entityManager, CityDistinct citydistinct) {
        if (citydistinct != null)
            return entityManager.merge(citydistinct);
        else
            return null;
    }
    
    public static String format(City city, CityDistinct citydistinct) {
        StringBuilder sb = new StringBuilder();
        if (city == null || city.getTitle() == null)
            return "";
        sb.append(city.getTitle());
        if (citydistinct != null && citydistinct.getTitle() != null && citydistinct.getTitle().length() > 0)
            sb.append(" (").append(citydistinct.getTitle()).append(")");
        return sb.toString();
    }
}
