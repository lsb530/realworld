package com.boki.realworld.common.exception.jwt;

import com.boki.realworld.common.exception.BadRequestException;

public class TokenExpiredException extends BadRequestException {

    public TokenExpiredException(String message) {
        super(message);
    }
}