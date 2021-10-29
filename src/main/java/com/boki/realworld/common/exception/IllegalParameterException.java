package com.boki.realworld.common.exception;

public class IllegalParameterException extends BadRequestException {

    public IllegalParameterException() {
        super("Illegal Parameters");
    }
}