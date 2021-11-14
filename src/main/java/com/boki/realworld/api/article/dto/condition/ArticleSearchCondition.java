package com.boki.realworld.api.article.dto.condition;

import lombok.AccessLevel;
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

}