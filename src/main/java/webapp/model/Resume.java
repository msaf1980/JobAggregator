/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import Utils.HTMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SortComparator;
import webapp.data.CityDistinctDAO;
import webapp.data.DateUtils;
import webapp.data.XMLParser;
import webapp.utils.Tools;

//import webapp.data.Job;

@ToString
@EqualsAndHashCode(of={"id"})
@Entity @Table(name = "resume")
@DynamicUpdate @SelectBeforeUpdate
public class Resume implements Serializable {

    @Id
    //@GeneratedValue
    @Getter @Setter
    protected Long id;
    
    @Getter @Setter
    protected Long owner_id;
    
    @Column(nullable = false)
    @Getter @Setter
    protected String name;
    
    @Column(nullable = false)
    @Getter @Setter
    protected String header;
    
    @Getter @Setter
    protected Long wanted_salary;
    
    @OneToOne
    @JoinColumn(name="currency_id")
    @Getter @Setter
    private Currency currency;
    
    @Getter @Setter
    protected java.util.Date birthday;
    
    @Getter @Setter
    protected java.util.Date add_day;
    
    @Getter @Setter
    protected java.util.Date mod_day;
    
    @OneToOne
    @JoinColumn(name="city_id")
    @Getter @Setter
    protected City city;

    @OneToOne
    @JoinColumn(name="citydistinct_id")
    @Getter @Setter
    private CityDistinct citydistinct;
    
    //@Column(name="education_id")
    @OneToOne
    @JoinColumn(name="education_id")
    @Getter @Setter
    protected Education education;
    
    //@Column(name="workingtype_id")
    @OneToOne
    @JoinColumn(name="workingtype_id")
    @Getter @Setter
    protected WorkingType workingtype;
    
    //@Column(name="schedule_id")
    @OneToOne
    @JoinColumn(name="schedule_id")
    @Getter @Setter
    protected Schedule schedule;
    
    //@Column(name="experience_id")
    @OneToOne
    @JoinColumn(name="experience_id")
    @Getter @Setter
    protected Experience experience;

    @Getter @Setter
    private String url;
    
    @Getter @Setter
    private String sex;
    
    @Getter @Setter
    private String marital_status;

    @Getter @Setter
    private Boolean has_child;
    
    @Basic(fetch = FetchType.LAZY)
    @Getter @Setter
    private String personal_qualities;
    
    @Getter @Setter
    private Boolean driver;
    @Getter @Setter
    private String driver_licenses;
    @Getter @Setter
    private Boolean smoke;
    @Getter @Setter
    private Boolean journey;
          
    @Basic(fetch = FetchType.LAZY)
    @Getter @Setter
    private String skills;
   
    //cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}
    //cascade = {CascadeType.REFRESH, CascadeType.REMOVE}
    //cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval=true
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    @OrderBy("from_day")
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter @Setter
    private List<Institution> institutionList = new ArrayList<Institution>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    //@OrderBy("PK.city_id")
    //@OrderBy("city.title, city_distinct.title")
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter @Setter
    private List<CitiesReference> citiesreferenceList = new ArrayList<CitiesReference>();
   
    //previous jobs
    // may be better use  ElementCollection - from_day may be null
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    @OrderBy("from_day")
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter @Setter
    private List<ResumeJob> resumejobList = new ArrayList<ResumeJob>();
   
    //resume rubric
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    @OrderBy("PK.jobrubric_id")
    //@OrderBy("job_rubric.title, job_specialty.title")
    @NotFound(action = NotFoundAction.IGNORE)
    @Getter @Setter
    private List<ResumeRubric> resumerubricList = new ArrayList<ResumeRubric>();
    
    public Resume() {
        
    }
    
    public Resume(JsonNode node, String site) {
        set(node, site);
    }
    
    public void set(JsonNode node, String site) {
        id = node.path("id").asLong();
        //if (id.equals(86747213))
        //    id = new Long(86747213);
        
        owner_id = node.path("owner_id").asLong();
        
        header = node.path("header").asText();
        personal_qualities = Tools.singleQuotesNull(node.path("personal_qualities").asText());
        skills = Tools.singleQuotesNull(node.path("skills").asText());

        wanted_salary = XMLParser.getLong(node.path("wanted_salary"));
        currency = XMLParser.getCurrency(node.path("currency"));

        birthday = DateUtils.parseDate(node.path("birthday").asText());
        add_day = DateUtils.parseDate(node.path("add_date").asText());
        mod_day = DateUtils.parseDate(node.path("mod_date").asText());
    
        //education = new Education(node.path("education"));
        education = XMLParser.getEducation(node.path("education"));
        workingtype = XMLParser.getWorkingType(node.path("working_type"));
        //schedule = new Schedule(node.path("schedule"));
        schedule =  XMLParser.getSchedule(node.path("schedule"));
        experience = XMLParser.getExperience(node.path("experience_length"));
        
        sex = node.path("sex").asText().substring(0, 1);
        marital_status = node.path("marital_status").asText();
        has_child = node.path("has_child").asBoolean();
        
        driver = node.path("is_driver").asBoolean();
        driver_licenses = XMLParser.getDriverLicenses(node.path("driver_licenses"));
        smoke = node.path("is_smoke").asBoolean();
        journey = node.path("is_journey").asBoolean();
        
        JsonNode contactNode = node.path("contact");
        if (contactNode.isContainerNode()) {
            name = contactNode.path("name").asText();
            city = XMLParser.getCity(contactNode.path("city"));
            citydistinct = XMLParser.getCityDistinct(contactNode.path("district"));
        }
        
        if (node.path("url").asText().length() > 0)
            url = site + node.path("url").asText();

        institutionList = XMLParser.getInstitutions(this, node);
        citiesreferenceList = XMLParser.getCityReferences(this, node.path("cities_references"));
        resumejobList = XMLParser.getResumeJobs(this, node.path("jobs"));
        resumerubricList = XMLParser.getResumeRubrics(this, node.path("rubrics"));
    }

    public String getSkillsHTML() {
        return HTMLUtils.formatWithNewLines(skills);
    }
    
    public String getPersonal_qualitiesHTML() {
        return HTMLUtils.formatWithNewLines(personal_qualities);
    }
    
    public String getSexDescription() {
        if (sex == null)
            return "";
        else if (sex.equals("m"))
            return "Муж.";
        else if (sex.equals("f"))
            return "Жен.";
        else
            return "";
    }
    
    public String getChildDescription() {
        return Tools.getBoolDescription(has_child);
    }
    
    public String getMaritalDescription() {
        if (marital_status == null)
            return "";
        else if (marital_status.equals("single"))
            return "Свобод.";
        else if (marital_status.equals("married")) 
            return "В браке";
        else if (marital_status.equals("not_important"))
            return "";
        else
            return marital_status;
    }
    
    public String getAge() {
        if (birthday == null)
            return "";
        else {
            long timeBetween = new Date().getTime() - birthday.getTime();
            double yearsBetween = timeBetween / 3.156e+10;
            int age = (int) Math.floor(yearsBetween);
            return new Integer(age).toString();
        }
    }
    
    public String getCityAndDistinct() {
        return CityDistinctDAO.format(city, citydistinct);
    }
    
    public List<Institution> getInstitutionMainList() {
        List<Institution> institutionMainList = new ArrayList<Institution>();
        for (Institution institution : institutionList) {
            if (! institution.getPK().getAdditional())
                institutionMainList.add(institution);
        }
        return institutionMainList;
    }
    
    public List<Institution> getInstitutionAdditionalList() {
        List<Institution> institutionAdditionalList = new ArrayList<Institution>();
        for (Institution institution : institutionList) {
            if (institution.getPK().getAdditional())
                institutionAdditionalList.add(institution);
        }
        return institutionAdditionalList;
    }

    public String getSalary() {
        if (wanted_salary == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(wanted_salary.toString());
        if (currency != null)
            sb.append(" ").append(currency.getTitle());
        return sb.toString();
    }
    
    /*
    public List<Institution> getCitiesReferenceListSorted() {
        List<Institution> institutionAdditionalList = new ArrayList<Institution>();
        for (Institution institution : institutionList) {
            if (institution.getPK().getAdditional())
                institutionAdditionalList.add(institution);
        }
        return institutionAdditionalList;
    }
    */
    
    public String getJourneyDescription() {
        return Tools.getBoolDescription(journey);
    }
    
    public String getSmokeDescription() {
        return Tools.getBoolDescription(smoke);
    }
}
