package Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeUtils {
	static final DateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");
	static final DateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    static final DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final DateFormat sdfDateMilliSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
    private static java.util.Date date0001 = null;
    
    static {
        try {
            date0001 = sdfDay.parse("0000-01-01");
        } catch (ParseException ex) {
            throw new RuntimeException("Constant date format error. What's going on ?");
        }
    }
            
    public static String formatDate(Date date) {
	    return sdfDate.format(date);
	}
    
	public static String formatDateMilliSec(Date date) {
	    return sdfDateMilliSec.format(date);
	}
	
	public static String formatDay(Date date) {
	    return sdfDay.format(date);
	}
	
	public static String formatMonth(Date date) {
	    return sdfMonth.format(date);
	}
    
    /**
     * Simple hack for avoid null date
     * @return 
     */
    public static java.util.Date get0001Year() {
        return date0001;
    }
    
    public static java.util.Date convertLess0001YearToNull(java.util.Date date) {
        if (date0001.compareTo(date) >= 0)
            return null;
        else
            return date;
    }
    
    public static String formatFromAndToMonthBlank(java.util.Date fromDate, java.util.Date toDate) {
        StringBuilder sb = new StringBuilder();
        if (fromDate != null)
            sb.append(formatMonth(fromDate));
        else if (toDate != null)
            sb.append(" - ");
        if (toDate != null) {
            sb.append(" / ");
            sb.append(formatMonth(toDate));
        }
            
        return sb.toString();
    }
    
    public static String formatFromAndToMonth(java.util.Date fromDate, java.util.Date toDate) {
        StringBuilder sb = new StringBuilder();
        if (fromDate != null)
            sb.append(formatMonth(fromDate));
        else if (toDate != null)
            sb.append(" - ");
        if (toDate != null) {
            sb.append(" / ");
            sb.append(formatMonth(toDate));
        } else if (fromDate != null) {
            sb.append(" / -");
        }
            
        return sb.toString();
    }
}
