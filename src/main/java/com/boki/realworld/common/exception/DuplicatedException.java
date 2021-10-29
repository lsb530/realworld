package com.boki.realworld.common.exception;

public class DuplicatedException extends RuntimeException {

    private static final String WRAPPING_MSG = "Duplicated Error: ";

    public DuplicatedException(String message) {
        super(WRAPPING_MSG + message);
    }
}