package com.boki.realworld.api.comment.exception;

import com.boki.realworld.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        super("This comment is Not Found");
    }
}