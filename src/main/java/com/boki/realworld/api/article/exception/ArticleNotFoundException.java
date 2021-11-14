package com.boki.realworld.api.article.exception;

import com.boki.realworld.common.exception.NotFoundException;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super("This article is not found");
    }
}