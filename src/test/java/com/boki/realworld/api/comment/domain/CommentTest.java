package com.boki.realworld.api.comment.domain;

import static com.boki.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.boki.realworld.fixture.UserFixture.USER1;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentTest {

    public static final String COMMENT_BODY = "comment body";

    private User author;
    private Article article;

    @BeforeEach
    void setUp() {
        author = User.builder()
            .email(USER1.getEmail())
            .username(USER1.getUsername())
            .password(USER1.getPassword())
            .build();
        article = Article.builder()
            .title(ARTICLE1.getTitle())
            .description(ARTICLE1.getDescription())
            .body(ARTICLE1.getBody())
            .author(author)
            .build();
    }

    @DisplayName("[댓글 생성] - body, article, author 가 없으면 예외 발생")
    @Test
    void invalidCreateComment() {
        assertAll(
            () -> assertThrows(IllegalParameterException.class,
                () -> Comment.builder()
                    .article(article)
                    .author(author)
                    .build()),
            () -> assertThrows(IllegalParameterException.class,
                () -> Comment.builder()
                    .body(COMMENT_BODY)
                    .author(author)
                    .build()),
            () -> assertThrows(IllegalParameterException.class,
                () -> Comment.builder()
                    .author(author)
                    .build())
        );
    }

    @DisplayName("[댓글 생성]")
    @Test
    void createComment() {
        Comment comment = Comment.builder()
            .body(COMMENT_BODY)
            .article(article)
            .author(author)
            .build();

        assertEquals(comment.getBody(), COMMENT_BODY);
        assertEquals(comment.getAuthor(), author);
        assertEquals(comment.getArticle(), article);
    }
}