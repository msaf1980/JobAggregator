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
import static java.lang.String.format;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
import webapp.utils.Tools;

@ToString
@Entity @Table(name = "institution")
@DynamicUpdate @SelectBeforeUpdate
public class Institution implements Serializable {

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.PK);
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
        final Institution other = (Institution) obj;
        if (!Objects.equals(this.PK, other.PK)) {
            return false;
        }
        return true;
    }

    
    @Embeddable
    public static class InstitutionPK implements Serializable {

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.resume_id);
            hash = 23 * hash + Objects.hashCode(this.institution);
            hash = 23 * hash + Objects.hashCode(this.form);
            hash = 23 * hash + Objects.hashCode(this.faculty);
            hash = 23 * hash + Objects.hashCode(this.specialty);
            hash = 23 * hash + Objects.hashCode(this.additional);
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
            final InstitutionPK other = (InstitutionPK) obj;
            if (!Objects.equals(this.institution, other.institution)) {
                return false;
            }
            if (!Objects.equals(this.form, other.form)) {
                return false;
            }
            if (!Objects.equals(this.faculty, other.faculty)) {
                return false;
            }
            if (!Objects.equals(this.specialty, other.specialty)) {
                return false;
            }
            if (!Objects.equals(this.resume_id, other.resume_id)) {
                return false;
            }
            if (!Objects.equals(this.additional, other.additional)) {
                return false;
            }
            return true;
        }
        
        @Getter @Setter
        private Long resume_id;
        
        @Getter @Setter
        private String institution;

        @Getter @Setter
        private String form;

        @Getter @Setter
        private String faculty;
        
        @Getter @Setter
        private String specialty;

        @Getter @Setter 
        private Boolean additional;

        public InstitutionPK() {
            institution = "";
            form = "";
            faculty = "";
            specialty = "";
            additional = false;
        }
    } 
    
    @EmbeddedId
    @Getter @Setter
    private InstitutionPK PK = new InstitutionPK();
    
    @ManyToOne
    @JoinColumn(name = "resume_id", insertable = false, updatable = false)
    //@NotFound(action = NotFoundAction.IGNORE)
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
    private String description;

    @Getter @Setter 
    private java.util.Date from_day;
            
    @Getter @Setter
    private java.util.Date to_day;

    public Institution() {

    }

    public Institution(@NotNull Resume resume, String institution_name, JsonNode node) {
        setInstitution(resume, institution_name, node);
    }

    public void setInstitution(Resume resume, @NotNull String institution_name, @NotNull JsonNode node) {
        setResume(resume);
        PK.setInstitution(institution_name);
        PK.setSpecialty(node.path("education_specialty").asText());
        description = XMLParser.getText(node.path("education_description"));
        PK.setAdditional(false);
    }

    public Institution(Resume resume, JsonNode entry, Boolean additional) {
        if (additional)
            setAdditionalEducation(resume, entry);
        else
            setInstitutionEntry(resume, entry);
    }

    public void setInstitutionEntry(@NotNull Resume resume, @NotNull JsonNode entry) {
        setResume(resume);
        from_day = DateUtils.parseDate(entry.path("date").path("from").path("date").asText());
        to_day = DateUtils.parseDate(entry.path("date").path("to").path("date").asText());
        if (from_day == null && to_day != null) {
            from_day = to_day;
            to_day = null;
        }
        //institution = XMLParser.getInstitution(entry.path("institution"));
        PK.setInstitution(entry.path("institution").path("title").asText());
        PK.setForm(entry.path("form").path("title").asText());
        this.city = XMLParser.getCity(entry.path("city"));
        //city = new City(entry.path("city"));
        PK.setFaculty(entry.path("faculty").path("title").asText());
        PK.setSpecialty(entry.path("specialty").path("title").asText());
        PK.setAdditional(false);
    }
    
    public void setAdditionalEducation(@NotNull Resume resume, @NotNull JsonNode entry) {
        setResume(resume);
        PK.setInstitution(entry.path("company_name").asText());
        PK.setSpecialty(entry.path("course_name").asText());
        from_day = DateUtils.parseDate(entry.path("finish_date").path("date").asText());
        this.city = XMLParser.getCity(entry.path("city"));
        //city = new City(entry.path("city"));
        PK.setAdditional(true);
    }
    
    public String getSpecialityName() {
        StringBuilder sb = new StringBuilder();
        if (PK.getFaculty() != null && PK.getFaculty().length() > 0)
            sb.append(PK.getFaculty());
        if (PK.getSpecialty() != null && PK.getSpecialty().length() > 0) {
            Tools.appendDelim(sb);
            sb.append(PK.getSpecialty());
        }
        return sb.toString();
    }
    
    /*
    public String getInstitutionName() {
        StringBuilder sb = new StringBuilder();
        if (PK.getInstitution() != null && PK.getInstitution().length() > 0)
            sb.append(PK.getInstitution());
        if (PK.getForm() != null && PK.getForm().length() > 0) {
            Tools.appendDelim(sb);
            sb.append(PK.getForm());
        }
        return sb.toString();
    }
    */
    
    public String getInstitution() {
        return PK.getInstitution();
    }
    
    public String getForm() {
        return PK.getForm();
    }
    
    public String getDates() {
        return DateTimeUtils.formatFromAndToMonthBlank(from_day, to_day);
    }
    
    public String getDescriptionHTML() {
        return HTMLUtils.formatWithNewLines(description);
    }
}
