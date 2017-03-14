/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import webapp.model.Resume;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;

public class Resumes {
    private ArrayList<Resume> resumes = new ArrayList<Resume>(); 
    
    private String site;
    
    //--------------------------            
    public ArrayList<Resume> getResumes() {
        return resumes;
    }
    //--------------------------
    public Resumes(String site) {
        this.site = site;
    }
    //--------------------------
    public void clearResumes() {
    }
    //--------------------------
    public void clear() {
        resumes.clear();
    }
    //--------------------------
    public void set(JsonNode node) {
        if (node.isArray()) {
            for (JsonNode entry : node) {
                //Resume resume = new Resume(entry, this);
                Resume resume = new Resume(entry, site);
                resumes.add(resume);
            }
        }        
    }
    //--------------------------
}
