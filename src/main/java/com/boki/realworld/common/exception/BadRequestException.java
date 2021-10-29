package com.boki.realworld.common.exception;

public class BadRequestException extends RuntimeException {

    private static final String WRAPPING_MSG = "Bad Request Error: ";

    public BadRequestException(String message) {
        super(WRAPPING_MSG + message);
    }
}