package com.boki.realworld.api.user.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedUsernameException extends DuplicatedException {

    public DuplicatedUsernameException() {
        super("This username is already being used");
    }
}