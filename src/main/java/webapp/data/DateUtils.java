/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author msv
 */
public class DateUtils {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    
    public static java.util.Date parseDate(String date) {
        if (date == null)
            return null;
        try {
            return format.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }
}
