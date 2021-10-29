package com.boki.realworld.api.user.exception;

import com.boki.realworld.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("This user is Not Found");
    }
}