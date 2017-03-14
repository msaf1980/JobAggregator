/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.forms;

public class ValidatorRules {
    public static final String WHITELIST_BLANK_PATTERN = "^[%-_,\\. \\/\\p{L}]*$";
    public static final String WHITELIST_PATTERN = "^[_-,\\. \\/\\p{L}]+$";
    public static final String BLACKLIST_PATTERN = "& |><";    
}
