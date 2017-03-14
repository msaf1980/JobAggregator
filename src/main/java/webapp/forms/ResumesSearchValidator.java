/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.forms;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.ValidationUtils;

@Component
public class ResumesSearchValidator  implements Validator {

    @Override
    public boolean supports(Class<?> paramClass) {
	return ResumesSearch.class.equals(paramClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "valid.location", "Location is required.");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "searchstr", "valid.searchstr", "Searchstr is required.");

        ResumesSearch form = (ResumesSearch) obj;
        if (form.getLocation() != null && !form.getLocation().matches(ValidatorRules.WHITELIST_BLANK_PATTERN)) {
            errors.rejectValue("location", "valid.error");
        }

        //ValidationUtils.rejectIfEmpty(errors, "vacancySearch", "valid.vacancySearch");
        if (form.getSearchstr() != null && !form.getSearchstr().matches(ValidatorRules.WHITELIST_BLANK_PATTERN)) {
            errors.rejectValue("searchstr", "valid.error");
        }
    }
    
}
