/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import webapp.model.CitiesReference;
import webapp.model.City;
import webapp.model.CityDistinct;
import webapp.model.Currency;
import webapp.model.Education;
import webapp.model.Experience;
import webapp.model.IdTitle;
import webapp.model.Institution;
import webapp.model.JobRubric;
import webapp.model.JobSpecialty;
import webapp.model.Resume;
import webapp.model.ResumeJob;
import webapp.model.ResumeRubric;
import webapp.model.Schedule;
import webapp.model.WorkingType;

/**
 *
 * @author msv
 */

public class XMLParser {
    public static City getCity(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            City city = new City(node);
            if (city.getId() == null && city.getTitle() == null)
                return null;
            else
                return city;
        }
    } 

    public static Schedule getSchedule(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            Schedule schedule = new Schedule(node);
            if (schedule.getId() == null && schedule.getTitle() == null)
                return null;
            else
                return schedule;
        }
    }
    
    public static WorkingType getWorkingType(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            WorkingType workingtype = new WorkingType(node);
            if (workingtype.getId() == null && workingtype.getTitle() == null)
                return null;
            else
                return workingtype;
        }
    }
    
    public static Education getEducation(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            Education education = new Education(node);
            if (education.getId() == null && education.getTitle() == null)
                return null;
            else
                return education;
        }
    }
    
    public static Experience getExperience(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            Experience experience = new Experience(node);
            if (experience.getId() == null && experience.getTitle() == null)
                return null;
            else
                return experience;
        }
    }
       
    public static Currency getCurrency(JsonNode node) {
        if (node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            Currency currency = new Currency(node);
            if (currency.getId() == null && currency.getTitle() == null)
                return null;
            else
                return currency;
        }
    }
    
    public static Long getLong(JsonNode node) {
        if (node.canConvertToLong())
            return node.asLong();
        else
            return null;
    }
    
    public static Integer getInt(JsonNode node) {
        if (node.canConvertToInt())
            return node.asInt();
        else
            return null;
    }

    public static String getText(JsonNode node) {
        if (! node.asText().equals(""))
            return node.asText();
        else
            return null;
    }
    
    public static CityDistinct getCityDistinct(JsonNode node) {
        CityDistinct city_distinct = new CityDistinct(node);
        if (city_distinct.getTitle() == null || city_distinct.getTitle().equals(""))
            return null;
        else
            return city_distinct;
    }
    
    public static List<ResumeRubric> getResumeRubrics(Resume resume, JsonNode node) {
        List<ResumeRubric> resumerubricList = new ArrayList<ResumeRubric>();
        if (node.isArray()) {
            for (JsonNode entry : node) {
                JobRubric job_rubric = new JobRubric(entry);
                JsonNode specNode = entry.path("specialities");
                boolean specialityBlank = true;
                if (specNode.isArray()) {
                    for (JsonNode specEntry : specNode) {
                        resumerubricList.add(new ResumeRubric(resume, job_rubric, new JobSpecialty(job_rubric, specEntry)));
                        specialityBlank = false;
                    }
                }
                if (specialityBlank) {
                    resumerubricList.add(new ResumeRubric(resume, job_rubric, null));
                }
            }
        }
        /*
        if (resumerubricList.size() == 0)
            return null;
        else
        */
            return resumerubricList;
    }

    public static List<CitiesReference> getCityReferences(Resume resume, JsonNode node) {
        List<CitiesReference> citiesreferenceList = new ArrayList<CitiesReference>();
        if (node.isArray()) {
            HashSet<CitiesReference> citiesReferenceMap = new HashSet<CitiesReference>();
            for (JsonNode entry : node) {
                City city = XMLParser.getCity(entry);
                //City city = new City(entry);
                if (city == null)
                    continue;
                JsonNode districtsNode = entry.path("districts");
                boolean districtsBlank = true;
                if (districtsNode.isArray()) {
                    for (JsonNode distEntry : districtsNode) {
                        CitiesReference citiesReference = new CitiesReference(resume, city, distEntry);
                        if (citiesReferenceMap.contains(citiesReference))
                            continue;
                        else
                            citiesReferenceMap.add(citiesReference);
                        citiesreferenceList.add(citiesReference);
                        //citiesreferenceList.add(new CitiesReference(resume, city, distEntry));
                        districtsBlank = false;
                    }
                }
                if (districtsBlank) {
                    CitiesReference citiesReference = new CitiesReference(resume, city);
                    if (citiesReferenceMap.contains(citiesReference))
                            continue;
                        else
                            citiesReferenceMap.add(citiesReference);
                    citiesreferenceList.add(citiesReference);
                    //citiesreferenceList.add(new CitiesReference(resume, city));
                }
            }
        }
        /*
        if (citiesreferenceList.size() == 0)
            return null;
        else
        */
        return citiesreferenceList;
    }
    
    public static List<ResumeJob> getResumeJobs(Resume resume, JsonNode node) {
        List<ResumeJob> resumejobList = new ArrayList<ResumeJob>();
        if (node.isArray()) {
            for (JsonNode entry : node) {
                ResumeJob resume_job = new ResumeJob(resume, entry);
                resumejobList.add(resume_job);
            }
        }
        /*
        if (resumejobList.size() == 0)
            return null;
        else
        */
        return resumejobList;
    }
    
    public static List<Institution> getInstitutions(Resume resume, JsonNode node) {
        List<Institution> institutionsList = new ArrayList<Institution>();
        JsonNode instNode = node.path("institution");
        if (! instNode.isMissingNode() && ! instNode.asText().equals("")) {
            //Institution institution = new Institution(InstNode.asText(), node.path("education_specialty").asText(), node.path("education_description").asText());
            Institution institution = new Institution(resume, instNode.asText(), node);
            if (institution.getPK().getInstitution().length() > 0)
                institutionsList.add(institution);
        }
        JsonNode instsNode = node.path("institutions");
        if (instsNode.isArray()) {
            for (JsonNode entry : instsNode) {
                Institution institution = new Institution(resume, entry, Boolean.FALSE);
                if (institution.getPK().getInstitution().length() > 0)
                    institutionsList.add(institution);
            }
        }
        
        instsNode = node.path("secondary_educations");
        if (instsNode.isArray()) {
            for (JsonNode entry : instsNode) {
                Institution institution = new Institution(resume, entry, Boolean.TRUE);
                if (institution.getPK().getInstitution().length() > 0)
                    institutionsList.add(institution);
            }
        }
        /*
        if (institutionsList.size() == 0)
            return null;
        else
        */
            return institutionsList;
    }
    
    public static String getDriverLicenses(JsonNode node) {
        if (node == null || ! node.isArray())
            return null;
        StringBuilder sb = new StringBuilder();
        for (JsonNode entry : node) {
            String driverLicense = entry.path("title").asText();
            if (driverLicense != null && driverLicense.length() > 0)
                sb.append(driverLicense);
        }
        if (sb.length() == 0)
            return null;
        return sb.toString();
    }
}
