package com.boki.realworld.api.user.exception;

import java.security.InvalidParameterException;

public class InvalidUserException extends InvalidParameterException {

    public InvalidUserException(String msg) {
        super(msg);
    }
}