package com.hugo.urlshortner.service.validation;

import com.hugo.urlshortner.service.validation.constraints.Url;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidation implements ConstraintValidator<Url, String> {

    @Override
    public void initialize(Url constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value)) {
            return false;
        }
        //TODO Look for a regular expression that works well
        //Pattern p = Pattern.compile("\"[A-Za-z0-9](([_\\\\.\\\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\\\.\\\\-]?[a-zA-Z0-9]+)*)\\\\.([A-Za-z]{2,})");
        //return p.matcher(value).matches();
        return true;
    }
}
