/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author msv
 */

public class IdPage {
    @Getter @Setter
    protected String id;
    
    @Getter @Setter
    protected String title;
    
    public IdPage(@NotNull String id, @NotNull String title) {
        this.id = id;
        this.title = title;
    }
    
}
