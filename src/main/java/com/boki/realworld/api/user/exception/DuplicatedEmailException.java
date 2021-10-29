package com.boki.realworld.api.user.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedEmailException extends DuplicatedException {

    public DuplicatedEmailException() {
        super("This email is already being used");
    }
}