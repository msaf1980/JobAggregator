/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.HashSet;
import java.util.List;
import javax.persistence.EntityManager;
import webapp.model.JobRubric;

/**
 *
 * @author msv
 */
public class JobRubricDAO {
    
    private static HashSet<Integer> jobrubricIds = new HashSet<Integer>();
    
    public static boolean contains(Integer jobrubricId) {
        return jobrubricIds.contains(jobrubricId);
    }
    
    public static void add(Integer jobrubricId) {
        jobrubricIds.add(jobrubricId);
    }
    
    public static void clear() {
        jobrubricIds.clear();
    }
    
    public static void merge(EntityManager entityManager, JobRubric jobrubric) {
        if (jobrubric != null) {
            if (jobrubricIds.contains(jobrubric.getId()))
                return;
            add(jobrubric.getId());
            entityManager.merge(jobrubric);
        } 
    }
    
    public static List<JobRubric> getByTitle(EntityManager entityManager, String title) {
        return entityManager.createQuery("SELECT jr FROM JobRubric jr WHERE title = ?1")
                .setParameter(1, title)
                    .getResultList();
    }
    
    public static List<JobRubric> getAllOrdered(EntityManager entityManager) {
        return entityManager.createQuery("SELECT jr FROM JobRubric jr ORDER BY jr.title")
                    .getResultList();
    }    
}
