/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.utils;

import java.util.Objects;

/**
 *
 * @author msv
 */
public class Tools {
    /**
     * return null if string with 2 single quotes
     * @param str
     * @return 
     */
    public static String singleQuotesNull(String str) {
        if (str == null || str.equals("") || str.equals("''"))
            return null;
        else
            return str;
    }
    
    public static boolean changedDate(java.util.Date oold, java.util.Date onew) {
        if (oold == null && onew == null)
            return false;
        if ((oold == null && onew != null) || (oold != null && onew == null))
            return false;
        return oold.compareTo(onew) != 0;
    }
    
    /**
     * Compare against null
     * @param oold
     * @param onew
     * @return true if one null and other not null
     */
    public static boolean changedNull(Object oold, Object onew) {
        return (oold == null && onew != null) || (oold != null && onew == null);
    }
    
    public static void appendDelim(StringBuilder sb) {
        if (sb.length() > 0) {
            char last = sb.charAt(sb.length() - 1);
            if (last == ',' || last == ';') {
                sb.append(' ');
            } else {
                sb.append("; ");
            }
        }
    }
    
    public static String getBoolDescription(Boolean value) {
        if (value == null)
            return "";
        else if (value)
            return "Да";
        else 
            return "Нет";
    }
}
