package com.boki.realworld.api.user.exception;

import java.security.InvalidParameterException;

public class InvalidUserException extends InvalidParameterException {

    public InvalidUserException() {
        super("Yourself can't be followed or unfollowed");
    }
}