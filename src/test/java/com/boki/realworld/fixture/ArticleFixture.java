package com.boki.realworld.fixture;

import static com.boki.realworld.fixture.TagFixture.TAG1;
import static com.boki.realworld.fixture.TagFixture.TAG2;
import static com.boki.realworld.fixture.TagFixture.TAG3;
import static com.boki.realworld.fixture.UserFixture.*;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.dto.request.CreateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest;
import java.util.List;

public class ArticleFixture {

    public static final Article ARTICLE1 = Article.builder()
        .id(1L)
        .title("how-to-train-your-dragon")
        .body("You have to believe")
        .description("Ever wonder how?")
        .author(USER1)
        .build();

    public static final Article ARTICLE2 = Article.builder()
        .id(2L)
        .title("how-to-make-huge-money")
        .body("Money Money Money")
        .description("Money is king?")
        .author(USER2)
        .build();

    public static final Article ARTICLE3 = Article.builder()
        .id(3L)
        .title("How to eat your dragon")
        .body("You have to believe")
        .description("Ever wonder how?")
        .author(USER3)
        .build();

    static {
        ARTICLE1.addTag(TAG1);
        ARTICLE1.addTag(TAG2);
        ARTICLE1.addTag(TAG3);
        //
        ARTICLE2.addTag(TAG2);
        //
        ARTICLE3.addTag(TAG1);
    }

    public static final CreateArticleRequest CREATE_ARTICLE_REQUEST =
        CreateArticleRequest.builder()
            .article(
                CreateArticleRequest.ArticleInfo.builder()
                    .title(ARTICLE1.getTitle())
                    .description(ARTICLE1.getDescription())
                    .body(ARTICLE1.getBody())
                    .tagList(List.of(TAG1.getName()))
                    .build())
            .build();

    public static final UpdateArticleRequest UPDATE_ARTICLE_REQUEST =
        UpdateArticleRequest.builder()
            .article(
                UpdateArticleRequest.ArticleInfo.builder()
                    .title("Update Title")
                    .description("Update Des")
                    .body("Update Body")
                    .build()
            ).build();
}