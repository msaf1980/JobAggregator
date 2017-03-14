package webapp.model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author msv
 */

import Utils.DateTimeUtils;
import Utils.HTMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import webapp.data.DateUtils;
import webapp.data.XMLParser;

@ToString
//@EqualsAndHashCode(exclude = { "description", "to_day" })
@Entity @Table(name = "resume_jobs")
@DynamicUpdate @SelectBeforeUpdate
public class ResumeJob implements Serializable {
    @Override
    public int hashCode() {
        int hash = 3;
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
        final ResumeJob other = (ResumeJob) obj;
        return Objects.equals(this.PK, other.PK);
    }
    
    public static class ResumeJobPK implements Serializable {

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.resume_id);
            hash = 79 * hash + Objects.hashCode(this.position);
            hash = 79 * hash + Objects.hashCode(this.company);
            hash = 79 * hash + Objects.hashCode(this.from_day);
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
            final ResumeJobPK other = (ResumeJobPK) obj;
            if (!Objects.equals(this.position, other.position)) {
                return false;
            }
            if (!Objects.equals(this.company, other.company)) {
                return false;
            }
            if (!Objects.equals(this.resume_id, other.resume_id)) {
                return false;
            }
            if (!Objects.equals(this.from_day, other.from_day)) {
                return false;
            }
            return true;
        }
        
        @Getter @Setter
        private Long resume_id;
        
        @Getter @Setter
        private String position;
    
        @Getter @Setter
        private String company;

        @Getter
        private java.util.Date from_day;
        
        public void setFrom_day(java.util.Date from_day) {
            if (from_day == null)
                this.from_day = DateTimeUtils.get0001Year();
            else
                this.from_day = from_day;
        }
        
        public ResumeJobPK() {
            
        }
    }
    
    @EmbeddedId
    @Getter @Setter
    private ResumeJobPK PK = new ResumeJobPK();
    
    @ManyToOne
    @JoinColumn(name="resume_id", insertable = false, updatable = false)
    @MapsId("resume_id")
    @Getter
    private Resume resume;

    public void setResume(@NotNull Resume resume) {
        this.resume = resume;
        PK.setResume_id(resume.getId());
    }
    
    @OneToOne
    @JoinColumn(name="city_id")
    @Getter @Setter
    private City city;
    
    @Getter @Setter
    private java.util.Date to_day;
            
    @Getter @Setter
    private String description;
    

    public ResumeJob() {
        
    }
    
    public ResumeJob(Resume resume, JsonNode node) {
        set(resume, node);
    }
        
    public void set(Resume resume, JsonNode node) {
        //PK.setResume(resume);
        setResume(resume);
        //PK.setCity(XMLParser.getCity(node.path("city")));
        city = XMLParser.getCity(node.path("city"));        
        PK.setFrom_day(DateUtils.parseDate(node.path("date").path("from").path("date").asText()));
        to_day = DateUtils.parseDate(node.path("date").path("to").path("date").asText());
        description = node.path("description").asText();
        PK.setPosition(node.path("position").path("title").asText());
        PK.setCompany(node.path("company").path("title").asText());
    }
    
    public String getDates() {
        java.util.Date from_day = DateTimeUtils.convertLess0001YearToNull(PK.getFrom_day());
        return DateTimeUtils.formatFromAndToMonth(from_day, to_day);
    }
    
    public String getDescriptionHTML() {
        return HTMLUtils.formatWithNewLines(description);
    }
}
