/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author msv
 */

@Embeddable
public class CitiesReferencePK implements Serializable {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.resume_id);
        hash = 41 * hash + Objects.hashCode(this.city_id);
        hash = 41 * hash + Objects.hashCode(this.citydistinct_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CitiesReferencePK other = (CitiesReferencePK) obj;
        if (! Objects.equals(this.resume_id, other.resume_id)) {
            return false;
        }
        if (! Objects.equals(this.city_id, other.city_id)) {
            return false;
        }
        if (! Objects.equals(this.citydistinct_id, other.citydistinct_id)) {
            return false;
        }
        return true;
    }
    
    @Column(nullable = false)
    @Getter
    @Setter
    private Long resume_id;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer city_id;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer citydistinct_id;

    public CitiesReferencePK() {

    }
}
