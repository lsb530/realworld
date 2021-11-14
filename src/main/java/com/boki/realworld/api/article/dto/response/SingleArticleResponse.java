package com.boki.realworld.api.article.dto.response;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.response.ProfileResponse.ProfileInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleArticleResponse {

    private ArticleInfo article;

    public static SingleArticleResponse of(Article article, User user) {
        return new SingleArticleResponse(ArticleInfo.of(article, user));
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ArticleInfo {

        private String slug;
        private String title;
        private String description;
        private String body;
        private Set<String> tagList;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
        private boolean favorite;
        private int favoriteCount;
        private ProfileInfo author;

        public static ArticleInfo of(Article article, User me) {
            return ArticleInfo.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(
                    article.getTagList().stream().map(Tag::getName).collect(Collectors.toSet()))
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .favorite(article.isFavorite(me))
                .favoriteCount(article.favoriteCount())
                .author(ProfileInfo.of(me, article.getAuthor()))
                .build();
        }
    }
}