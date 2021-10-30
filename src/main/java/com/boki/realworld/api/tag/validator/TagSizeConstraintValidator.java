package com.boki.realworld.api.tag.validator;

import com.boki.realworld.api.tag.domain.Tag;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TagSizeConstraintValidator implements ConstraintValidator<TagSizeConstraint, List<Tag>> {

    @Override
    public boolean isValid(List<Tag> values, ConstraintValidatorContext context) {
        return values.size() >= 2 && values.size() <= 15;
    }
}