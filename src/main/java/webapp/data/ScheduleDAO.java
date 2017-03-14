/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import webapp.model.Schedule;

/**
 *
 * @author msv
 */
public class ScheduleDAO {
    public static Schedule merge(EntityManager entityManager, Schedule schedule) {
        if (schedule == null || schedule.getTitle() == null) 
            return null;
        //if (! entityManager.contains(city))
        if (schedule.getId() == null) {
            Schedule scheduleByTitle = getByTitle(entityManager, schedule.getTitle());
            if (scheduleByTitle == null)
                return null;
            else {
                schedule.setId(scheduleByTitle.getId());
                return scheduleByTitle;
            }
        }
        return entityManager.merge(schedule);
    }
    
    public static Schedule getByTitle(EntityManager entityManager, String title) {
        try {
        Schedule schedule = (Schedule) entityManager.createQuery("SELECT s FROM Schedule s WHERE title = ?1")
                .setParameter(1, title)
                    .getSingleResult();
        return schedule;
        } catch (NoResultException e) {
            return null;
        }
    }
}
