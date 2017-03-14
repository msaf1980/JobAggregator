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

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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

@ToString
@Entity @Table(name = "resume_rubrics")
@DynamicUpdate @SelectBeforeUpdate
public class ResumeRubric implements Serializable, Comparable<ResumeRubric> {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.PK);
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
        final ResumeRubric other = (ResumeRubric) obj;
        if (!Objects.equals(this.PK, other.PK)) {
            return false;
        }
        return true;
    }
    
    @Embeddable
    public static class ResumeRubricPK implements Serializable {

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + Objects.hashCode(this.resume_id);
            hash = 89 * hash + Objects.hashCode(this.jobrubric_id);
            hash = 89 * hash + Objects.hashCode(this.jobspecialty_id);
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
            final ResumeRubricPK other = (ResumeRubricPK) obj;
            if (!Objects.equals(this.resume_id, other.resume_id)) {
                return false;
            }
            if (!Objects.equals(this.jobrubric_id, other.jobrubric_id)) {
                return false;
            }
            if (!Objects.equals(this.jobspecialty_id, other.jobspecialty_id)) {
                return false;
            }
            return true;
        }
        @Getter @Setter
        private Long resume_id;
          
        @Getter @Setter
        private Integer jobrubric_id;

        @Getter @Setter
        private Integer jobspecialty_id;
        
        public ResumeRubricPK() {
            
        }
    }
    
    @EmbeddedId
    @Getter @Setter
    private  ResumeRubricPK PK = new  ResumeRubricPK();
    
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
    @JoinColumn(name="jobrubric_id")
    @MapsId("jobrubric_id")
    @Getter @Setter
    private JobRubric jobrubric;

    public void setJobrubric(@NotNull JobRubric job_rubric) {
        this.jobrubric = job_rubric;
        PK.setJobrubric_id(job_rubric.getId());
    }
    
    @OneToOne
    @JoinColumn(name="jobspecialty_id")
    @MapsId("jobspecialty_id")
    @Getter
    private JobSpecialty jobspecialty;
    
    public void setJobspecialty(@NotNull JobSpecialty job_specialty) {
        this.jobspecialty = job_specialty;
        PK.setJobspecialty_id(job_specialty.getId());
    }
    
    public ResumeRubric() {
        
    }

    public ResumeRubric(Resume resume, JobRubric job_rubric) {
        set(resume, job_rubric);
    }
        
    public void set(Resume resume, JobRubric job_rubric) {
        set(resume, job_rubric, null);
    }
    
    public ResumeRubric(Resume resume, JobRubric job_rubric, JobSpecialty job_specialty) {
        set(resume, job_rubric, job_specialty);
    }
        
    public void set(Resume resume, JobRubric job_rubric, JobSpecialty job_specialty) {
        //PK.setResume(resume);
        setResume(resume);
        //PK.setJob_rubric(jobrubric);
        setJobrubric(job_rubric);
        //PK.setJob_specialty(jobspecialty);
        if (job_specialty == null)
            setJobspecialty(new JobSpecialty(job_rubric, null));
        else
            setJobspecialty(job_specialty);
    }
    
    public int compareTo(ResumeRubric rr2) {
        if (rr2 == null)
            return 1;
        int r = getJobrubric().getTitle().compareTo(rr2.getJobrubric().getTitle());
        if (r == 0) {
            return getJobspecialty().getTitle().compareTo(rr2.getJobspecialty().getTitle());
        } else
            return r;
    }
}
