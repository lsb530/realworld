package com.boki.realworld.api.article.dto.condition;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSearchCondition {

    private String tag;
    private String author;
    private String favorited;
    private Long limit = 20L;
    private Long offset = 0L;

    @Builder
    public ArticleSearchCondition(String tag, String author, String favorited, Long limit,
        Long offset) {
        this.tag = tag;
        this.author = author;
        this.favorited = favorited;
        this.limit = limit;
        this.offset = offset;
    }
}