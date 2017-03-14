/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.model;

/**
 *
 * @author msv
 */

import java.util.Comparator;
import javax.validation.constraints.NotNull;
import webapp.model.CitiesReference;

public class CitiesReferenceComparator implements Comparator<CitiesReference> {
    @Override
    public int compare(CitiesReference cr1, CitiesReference cr2) {
        if (cr1 == null) {
            if (cr2 == null)
                return 0;
            else
                return -1;
        } else if (cr2 == null)
            return 1;
        return cr1.compareTo(cr2);
    }
}
