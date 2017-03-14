/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import javax.persistence.EntityManager;
import webapp.model.Experience;

/**
 *
 * @author msv
 */
public class ExperienceDAO {
    public static Experience merge(EntityManager entityManager, Experience experience) {
        if (experience != null)
            return entityManager.merge(experience);
        else
            return null;
    }
}
