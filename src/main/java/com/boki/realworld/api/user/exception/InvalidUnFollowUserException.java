package com.boki.realworld.api.user.exception;

import java.security.InvalidParameterException;

public class InvalidUnFollowUserException extends InvalidParameterException {

    public InvalidUnFollowUserException() {
        super("This user hasn't been followed yet");
    }
}