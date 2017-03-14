/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import webapp.model.WorkingType;

/**
 *
 * @author msv
 */
public class WorkingTypeDAO {
    public static WorkingType merge(EntityManager entityManager, WorkingType workingtype) {
        if (workingtype == null || workingtype.getTitle() == null) 
            return null;
        //if (! entityManager.contains(city))
        if (workingtype.getId() == null) {
            List<WorkingType> workingtypeByTitle = getByTitle(entityManager, workingtype.getTitle());
            if (workingtypeByTitle.size() == 0)
                return null;
            else {
                workingtype.setId(workingtypeByTitle.get(0).getId());
                return workingtypeByTitle.get(0);
            }
        }
        return entityManager.merge(workingtype);
    }
    
    public static List<WorkingType> getByTitle(EntityManager entityManager, String title) {
        List workingtypeList = entityManager.createQuery("SELECT s FROM Schedule s WHERE title = ?1")
                .setParameter(1, title)
                    .getResultList();
        return workingtypeList;
    }
}
