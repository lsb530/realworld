package com.boki.realworld.api.article.dto.request;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.user.domain.User;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateArticleRequest {

    @Valid
    private ArticleInfo article;

    public Article toEntity(User author) {
        return Article.builder()
            .title(article.getTitle())
            .description(article.getDescription())
            .body(article.getBody())
            .slug(article.getTitle())
            .author(author)
            .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ArticleInfo {

        @NotEmpty(message = "title cannot be empty.")
        private String title;

        @NotEmpty(message = "description cannot be empty.")
        private String description;

        @NotEmpty(message = "body cannot be empty.")
        private String body;

        private List<String> tagList;
    }
}