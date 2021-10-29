package com.boki.realworld.api.user.exception;

import com.boki.realworld.common.exception.BadRequestException;

public class WrongPasswordException extends BadRequestException {

    public WrongPasswordException() {
        super("wrong password");
    }
}