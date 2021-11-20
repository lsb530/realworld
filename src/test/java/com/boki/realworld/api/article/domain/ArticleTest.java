package com.boki.realworld.api.article.domain;

import static com.boki.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.boki.realworld.fixture.ArticleFixture.ARTICLE2;
import static com.boki.realworld.fixture.ArticleFixture.ARTICLE3;
import static com.boki.realworld.fixture.TagFixture.TAG1;
import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.boki.realworld.api.article.exception.DuplicatedFavoriteException;
import com.boki.realworld.api.article.exception.InvalidUnFavoriteException;
import com.boki.realworld.api.user.exception.InvalidUserException;
import com.boki.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @BeforeEach
    void setUp() {
        ARTICLE1.getFavoriteList().clear();
        ARTICLE2.getFavoriteList().clear();
        ARTICLE3.getFavoriteList().clear();
    }

    @DisplayName("[글 생성] - 제목이 없으면 예외 발생")
    @Test
    void createInvalidArticle1() {
        assertThrows(IllegalParameterException.class,
            () -> Article.builder()
                .description("des")
                .body("body")
                .author(USER1)
                .build()
        );
    }

    @DisplayName("[글 생성] - 설명이 없으면 예외 발생")
    @Test
    void createInvalidArticle2() {
        assertThrows(IllegalParameterException.class,
            () -> Article.builder()
                .title("title")
                .body("body")
                .author(USER1)
                .build()
        );
    }

    @DisplayName("[글 생성] - 바디가 없으면 예외 발생")
    @Test
    void createInvalidArticle3() {
        assertThrows(IllegalParameterException.class,
            () -> Article.builder()
                .title("title")
                .description("des")
                .author(USER1)
                .build()
        );
    }

    @DisplayName("[글 생성] - 작가가 없으면 예외 발생")
    @Test
    void createInvalidArticle4() {
        assertThrows(IllegalParameterException.class,
            () -> Article.builder()
                .title("title")
                .description("des")
                .body("body")
                .build()
        );
    }

    @DisplayName("[글 생성]")
    @Test
    void createArticle() {
        Article article = Article.builder()
            .title("How to train your dragon?")
            .description("des")
            .body("body")
            .author(USER1)
            .build();
        assertEquals("How to train your dragon?", article.getTitle());
        assertEquals("how-to-train-your-dragon", article.getSlug());
    }

    @DisplayName("[글 수정]")
    @Test
    void update() {
        String title = "Updated Title";
        String description = "Updated Description";
        String body = "Updated Body";
        ARTICLE1.update(title, description, body);
        assertEquals(title, ARTICLE1.getTitle());
        assertEquals(description, ARTICLE1.getDescription());
        assertEquals(body, ARTICLE1.getBody());
        assertEquals("updated-title", ARTICLE1.getSlug());
    }

    @DisplayName("[태그 추가]")
    @Test
    void addTag() {
        assertEquals(1, ARTICLE2.getTagList().size());
        ARTICLE2.addTag(TAG1);
        assertEquals(2, ARTICLE2.getTagList().size());
    }

    @DisplayName("[글 좋아요] - 본인이 본인 글에 좋아요 할 때 예외 발생")
    @Test
    void favoriteMe() {
        assertThrows(InvalidUserException.class,
            () -> ARTICLE1.favorite(USER1));
    }

    @DisplayName("[글 좋아요] - 이미 좋아요 한 글을 좋아요 할 때 중복 예외 발생")
    @Test
    void favoriteDuplicate() {
        assertThrows(DuplicatedFavoriteException.class,
            () -> {
                ARTICLE1.favorite(USER2);
                ARTICLE1.favorite(USER2);
            });
    }

    @DisplayName("[글 좋아요]")
    @Test
    void favorite() {
        ARTICLE1.favorite(USER2);
        assertTrue(ARTICLE1.getFavoriteList().contains(USER2));
    }

    @DisplayName("[글 싫어요] - 본인이 본인 글에 싫어요 할 때 예외 발생")
    @Test
    void unFavoriteMe() {
        assertThrows(InvalidUserException.class,
            () -> ARTICLE1.unFavorite(USER1));
    }

    @DisplayName("[글 싫어요] - 좋아요 누르지 않은 글을 싫어요 할 때 예외 발생")
    @Test
    void invalidUnFavorite() {
        assertThrows(InvalidUnFavoriteException.class,
            () -> ARTICLE3.unFavorite(USER2));
    }

    @DisplayName("[글 싫어요]")
    @Test
    void unFavorite() {
        ARTICLE1.favorite(USER2);
        ARTICLE1.unFavorite(USER2);
        assertFalse(ARTICLE1.getFavoriteList().contains(USER2));
    }
}