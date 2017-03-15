/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.forms;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResumesSearch implements Serializable {
    @Getter @Setter
    private String location;
    
    private void setLocationBlank() {
        location = "Все";
    }
    
    @Getter
    private String searchstr;
    
    public void setSearchstr(String searchstr) {
        this.searchstr = searchstr.toLowerCase();
    }
    
    public ResumesSearch(String location, String searchstr) {
        clear();
        if (location != null)
            this.location = location;
        if (searchstr != null)
            this.searchstr = searchstr;
    }
    
    public ResumesSearch() {
        clear();
    }
    
    
    public void clear() {
       setLocationBlank();
       searchstr = null;
    }
    
    public String getFilter() {
        StringBuilder sb = new StringBuilder();
        try {
            if (location != null && ! location.equals("Все") && ! location.equals("")) {
                sb.append("?city=").append(URLEncoder.encode(location, "UTF-8"));
            }    
            if (searchstr != null && ! searchstr.equals("")) {
                if (sb.length() == 0)
                    sb.append("?");
                else
                    sb.append("&");
                sb.append("search=").append(URLEncoder.encode(searchstr, "UTF-8"));
            }
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
        if (sb.length() == 0)
            return null;
        return sb.toString();
    }

}
