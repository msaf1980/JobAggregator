/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

import java.util.Comparator;
import webapp.model.ResumeRubric;

/**
 *
 * @author msv
 */
public class ResumeRubricComparator implements Comparator<ResumeRubric> {
    @Override
    public int compare(ResumeRubric rr1, ResumeRubric rr2) {
        if (rr1 == null) {
            if (rr2 == null)
                return 0;
            else
                return -1;
        } else if (rr2 == null)
            return 1;
        return rr1.compareTo(rr2);
    }    
}
