package com.boki.realworld.api.user.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedFollowException extends DuplicatedException {

    public DuplicatedFollowException() {
        super("This user is already followed");
    }
}