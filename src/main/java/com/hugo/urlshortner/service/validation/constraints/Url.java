package com.hugo.urlshortner.service.validation.constraints;

import com.hugo.urlshortner.service.validation.UrlValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {UrlValidation.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Url {

    String message() default "Invalid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
