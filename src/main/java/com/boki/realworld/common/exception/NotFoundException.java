package com.boki.realworld.common.exception;

public class NotFoundException extends RuntimeException {

    private static final String WRAPPING_MSG = "Not Found Error: ";

    public NotFoundException(String message) {
        super(WRAPPING_MSG + message);
    }
}