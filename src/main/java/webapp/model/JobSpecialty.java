/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author msv
 */

@ToString
@EqualsAndHashCode(of={"id"})
@Entity @Table(name = "job_specialty")
@DynamicUpdate @SelectBeforeUpdate
public class JobSpecialty extends IdTitleNullable {
    
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="jobrubric_id")
    @Getter @Setter
    private JobRubric job_rubric;
    
    public JobSpecialty() {
        
    }
    
    public JobSpecialty(JobRubric jobrubric, JsonNode node) {
        set(jobrubric, node);
    }
    
    
    public void set(JobRubric jobrubric, JsonNode node) {
        set(node);
        if (this.id == null) {
            this.id = 0;
            this.title = null;
            this.job_rubric = new JobRubric(0, null);
        } else
            this.job_rubric = jobrubric;
    }
}
