/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import webapp.data.XMLParser;

/**
 *
 * @author msv
 */

@ToString
@Entity @Table(name = "cities_reference")
@DynamicUpdate @SelectBeforeUpdate
public class CitiesReference implements Serializable, Comparable<CitiesReference> {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.PK);
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
        final CitiesReference other = (CitiesReference) obj;
        return Objects.equals(this.getPK(), other.getPK());
    }

    @EmbeddedId
    @Getter @Setter
    private CitiesReferencePK PK = new CitiesReferencePK();
    
    //@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @ManyToOne
    @JoinColumn(name = "resume_id", insertable = false, updatable = false)
    @MapsId("resume_id")
    @Getter
    private Resume resume;
    
    public void setResume(@NotNull Resume resume) {
        this.resume = resume;
        PK.setResume_id(resume.getId());
    }

    @OneToOne
    @JoinColumn(name="city_id")
    @MapsId("city_id")
    @Getter
    private City city;
    
    public void setCity(@NotNull City city) {
        this.city = city;
        PK.setCity_id(city.getId());
    }

    //@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @OneToOne
    @JoinColumn(name="citydistinct_id")
    @MapsId("citydistinct_id")
    @Getter
    private CityDistinct citydistinct;
    
    public void setCitydistinct(@NotNull CityDistinct citydistinct) {
        this.citydistinct = citydistinct;
        PK.setCitydistinct_id(citydistinct.getId());
    }
    
    public CitiesReference() {
        
    }
    
    public CitiesReference(Resume resume, City city, JsonNode node) {
        set(resume, city, node);
    }
    
    public void set(Resume resume, City city, JsonNode node) {
        CityDistinct citydistinct = new CityDistinct(node);
        set(resume, city, citydistinct);
    }
    
    public CitiesReference(Resume resume, City city) {
        //fix for nullable CityDistinct - can't be part of PK
        set(resume, city, (CityDistinct) null);
    }
    
    public void set(Resume resume, City city, CityDistinct citydistinct) {
        //PK.setResumeid(resume.getId());
        setResume(resume);
        //PK.setCityid(city.getId());
        setCity(city);
        //PK.setCitydistinctid(new CityDistinct(node).getId());
        if (citydistinct == null)
            setCitydistinct(new CityDistinct(0, null));
        else
            setCitydistinct(citydistinct);
    }
    
    public int compareTo(CitiesReference cr2) {
        if (cr2 == null)
            return 1;
        int r = getCity().getTitle().compareTo(cr2.getCity().getTitle());
        if (r == 0) {
            if (getCitydistinct().getTitle() == null) {
                if (cr2.getCitydistinct().getTitle() == null)
                    return r;
                else
                    return -1;
            } else if (cr2.getCitydistinct().getTitle() == null)
                return 1;
            return getCitydistinct().getTitle().compareTo(cr2.getCitydistinct().getTitle());
        } else
            return r;
    }
}
