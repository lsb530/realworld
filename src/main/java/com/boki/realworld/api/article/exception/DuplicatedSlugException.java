package com.boki.realworld.api.article.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedSlugException extends DuplicatedException {

    public DuplicatedSlugException() {
        super("This article's slug is already being used");
    }
}