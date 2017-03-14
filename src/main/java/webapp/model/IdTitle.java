/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@ToString
@EqualsAndHashCode(of={"id"})
public abstract class IdTitle  implements Serializable {

    @Id
    //@GeneratedValue
    @Getter @Setter
    protected Integer id;
    
    @Getter @Setter
    @Column(nullable = false)
    protected String title;
   
    public void set(Integer id, String title) {
        this.id = id;
        this.title = title;
    }
            
    public void set(JsonNode node) {
        id = null;
        title = null;
        if (node == null || node.isNull() || node.isMissingNode())
            return;
        if (node.path("id").isInt())
            id = node.path("id").asInt();
        if (! node.path("title").isMissingNode())
            title = node.path("title").asText();
    }

    /*
    public boolean update(IdTitle idTitle_new) {
        if (idTitle_new == null || ! idTitle_new.getId().equals(id))
            return false;
        if (! title.equals(idTitle_new.title)) {
            this.setTitle(idTitle_new.title);
            return true;
        }
        return false;
    }
    */
    
    public boolean equalsId(IdTitle idTitle) {
        if (idTitle != null && id.equals(idTitle.getId()))
            return true;
        return false;
    }
    
    public boolean equalsTitle(IdTitle idTitle) {
        if (idTitle != null && title.equals(idTitle.getTitle()))
            return true;
        return false;
    }
}
