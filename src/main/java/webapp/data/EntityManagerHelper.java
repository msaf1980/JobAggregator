/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
Example
    EntityManager entityManager = EntityManagerHelper.getEntityManager();

or

    public EntityManager initEntityManager() {
        try {
            return EntityManagerHelper.getEntityManager();
        } catch (Exception e) {
            connection_error = e.getMessage();
            throw new RuntimeException("Couldn't init database connection: " + connection_error);
        } 
    }

    ..
    EntityManager entityManager = initEntityManager();
    ..
*/

public class EntityManagerHelper {
    private static final EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocal;

    static {
        emf = Persistence.createEntityManagerFactory("JobAggregator");
        threadLocal = new ThreadLocal<EntityManager>();
    }

    public static EntityManager getEntityManagerSingleThread() {
        return emf.createEntityManager();
    }
            
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();

        if (em == null) {
            em = emf.createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            em.close();
            threadLocal.set(null);
        }
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }
}
