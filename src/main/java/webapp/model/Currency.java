/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 *
 * @author msv
 */

@Entity @Table(name = "currency")
@DynamicUpdate @SelectBeforeUpdate
public class Currency extends IdTitle {

    public Currency() {
        
    }
    
    public Currency(JsonNode node) {
        set(node);
    }
}
