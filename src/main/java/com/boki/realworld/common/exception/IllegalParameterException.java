package com.boki.realworld.common.exception;

public class IllegalParameterException extends BadRequestException {

    private static final String WRAPPING_MSG = "Illegal Parameters: ";

    public IllegalParameterException(String message) {
        super(WRAPPING_MSG + message);
    }

    public IllegalParameterException() {
        super(WRAPPING_MSG);
    }
}