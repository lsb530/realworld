package com.boki.realworld.api.tag.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedTagNameException extends DuplicatedException {

    public DuplicatedTagNameException() {
        super("This tag name is already being used");
    }
}