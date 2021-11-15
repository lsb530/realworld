package com.boki.realworld.api.comment.dto.request;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.user.domain.User;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCommentRequest {

    @Valid
    private CommentInfo comment;

    public Comment toEntity(Article article, User author) {
        return Comment.builder()
            .body(comment.getBody())
            .author(author)
            .article(article)
            .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CommentInfo {

        @NotBlank(message = "body cannot be empty")
        private String body;
    }
}