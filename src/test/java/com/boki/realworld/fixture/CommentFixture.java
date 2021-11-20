package com.boki.realworld.fixture;

import static com.boki.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;

import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.comment.dto.request.CreateCommentRequest;
import com.boki.realworld.api.comment.dto.request.CreateCommentRequest.CommentInfo;

public class CommentFixture {

    private static final String COMMENT_BODY = "comment";

    public static final Comment COMMENT1 = Comment.builder()
        .id(1L)
        .body(COMMENT_BODY)
        .article(ARTICLE1)
        .author(USER1)
        .build();

    public static final Comment COMMENT2 = Comment.builder()
        .id(2L)
        .body(COMMENT_BODY)
        .article(ARTICLE1)
        .author(USER2)
        .build();

    public static final CreateCommentRequest CREATE_COMMENT_REQUEST =
        CreateCommentRequest.builder()
            .comment(
                CommentInfo.builder()
                    .body(COMMENT_BODY)
                    .build()
            ).build();
}