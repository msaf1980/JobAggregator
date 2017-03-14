/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 *
 * @author msv
 */

@Entity @Table(name = "job_rubric")
@DynamicUpdate @SelectBeforeUpdate
public class JobRubric extends IdTitleNullable {

    public JobRubric() {
        
    }
    
    public JobRubric(JsonNode node) {
        set(node);
    }
    
    public JobRubric(Integer id, String title) {
        set(id, title);
    }
}
