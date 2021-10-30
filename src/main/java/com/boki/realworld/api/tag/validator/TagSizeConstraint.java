package com.boki.realworld.api.tag.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = TagSizeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TagSizeConstraint {
    String message()
        default "Tag name should be between 2-15";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}