/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.HashSet;
import javax.persistence.EntityManager;
import webapp.model.JobSpecialty;

/**
 *
 * @author msv
 */
public class JobSpecialtyDAO {
    
    private static HashSet<Integer> jobspecialtyIds = new HashSet<Integer>();
        
    public static boolean contains(Integer jobspecialtyId) {
        return jobspecialtyIds.contains(jobspecialtyId);
    }
    
    public static void add(Integer jobspecialtyId) {
        jobspecialtyIds.add(jobspecialtyId);
    }
    
    public static void clear() {
        jobspecialtyIds.clear();
    }
    
    public static void merge(EntityManager entityManager, JobSpecialty jobspecialty) {
        if (jobspecialty != null) {
            JobRubricDAO.merge(entityManager, jobspecialty.getJob_rubric());
            if (jobspecialtyIds.contains(jobspecialty.getId()))
                return;
            add(jobspecialty.getId());
            entityManager.merge(jobspecialty);
        }
    }    
}
