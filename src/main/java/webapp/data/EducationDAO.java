/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import javax.persistence.EntityManager;
import webapp.model.Education;

/**
 *
 * @author msv
 */
public class EducationDAO {
    public static Education merge(EntityManager entityManager, Education education) {
        if (education != null)
            return entityManager.merge(education);
        else
            return null;
    }
}
