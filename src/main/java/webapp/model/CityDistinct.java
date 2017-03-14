/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

@Entity @Table(name = "city_distinct")
@DynamicUpdate @SelectBeforeUpdate
public class CityDistinct extends IdTitleNullable {

    public CityDistinct() {
    
    }
    
    public CityDistinct(JsonNode node) {
        set(node);
    }
    
    public CityDistinct(Integer id, String title) {
        set(id, title);
    }
}
