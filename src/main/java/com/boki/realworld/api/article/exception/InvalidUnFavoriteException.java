package com.boki.realworld.api.article.exception;

import java.security.InvalidParameterException;

public class InvalidUnFavoriteException extends InvalidParameterException {

    public InvalidUnFavoriteException() {
        super("This article hasn't been favorited yet");
    }
}