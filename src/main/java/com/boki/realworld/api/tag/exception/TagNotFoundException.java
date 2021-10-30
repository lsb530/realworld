package com.boki.realworld.api.tag.exception;

import com.boki.realworld.common.exception.NotFoundException;

public class TagNotFoundException extends NotFoundException {

    public TagNotFoundException() {
        super("This tag is Not Found");
    }
}