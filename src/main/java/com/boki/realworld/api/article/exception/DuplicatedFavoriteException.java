package com.boki.realworld.api.article.exception;

import com.boki.realworld.common.exception.DuplicatedException;

public class DuplicatedFavoriteException extends DuplicatedException {

    public DuplicatedFavoriteException() {
        super("You already have favorited this article");
    }
}