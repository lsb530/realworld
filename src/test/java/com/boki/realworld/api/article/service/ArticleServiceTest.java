package com.boki.realworld.api.article.service;

import static com.boki.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.boki.realworld.fixture.ArticleFixture.ARTICLE2;
import static com.boki.realworld.fixture.ArticleFixture.CREATE_ARTICLE_REQUEST;
import static com.boki.realworld.fixture.ArticleFixture.UPDATE_ARTICLE_REQUEST;
import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.domain.ArticleQueryRepository;
import com.boki.realworld.api.article.domain.ArticleRepository;
import com.boki.realworld.api.article.dto.condition.ArticleSearchCondition;
import com.boki.realworld.api.article.dto.request.CreateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest;
import com.boki.realworld.api.article.dto.response.SingleArticleResponse.ArticleInfo;
import com.boki.realworld.api.article.exception.ArticleNotFoundException;
import com.boki.realworld.api.article.exception.DuplicatedFavoriteException;
import com.boki.realworld.api.article.exception.DuplicatedSlugException;
import com.boki.realworld.api.article.exception.InvalidUnFavoriteException;
import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.tag.service.TagService;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.exception.InvalidUserException;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.exception.BadRequestException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleQueryRepository articleQueryRepository;

    @Mock
    private UserService userService;

    @Mock
    private TagService tagService;

    private List<String> tagList;

    private List<Tag> tags;

    private Article myArticle;

    private Article otherArticle;

    private User me;

    @BeforeEach
    void setUp() {
        tagList = List.of("reactjs", "angularjs");
        tags = tagList.stream().map(Tag::new).collect(Collectors.toList());
        myArticle = ARTICLE1;
        otherArticle = ARTICLE2;
        myArticle.getFavoriteList().clear();
        otherArticle.getFavoriteList().clear();
        me = USER1;
        given(userService.getUserFrom(any())).willReturn(me);
    }

    @DisplayName("[??? ??????] - ???????????? ???????????? ?????? ??????")
    @Test
    void createArticleDuplicateSlug() {
        given(articleRepository.existsArticleBySlug(any())).willReturn(true);
        assertThrows(DuplicatedSlugException.class,
            () -> articleService.create(CREATE_ARTICLE_REQUEST, USER_TOKEN));
    }

    @DisplayName("[??? ??????]")
    @Test
    void createArticle() {
        given(articleRepository.existsArticleBySlug(any())).willReturn(false);
        given(tagService.saveAndFindAll(anyList())).willReturn(tags);
        given(articleRepository.save(any(Article.class))).willReturn(myArticle);
        CreateArticleRequest.ArticleInfo request = CREATE_ARTICLE_REQUEST.getArticle();
        ArticleInfo response = articleService.create(CREATE_ARTICLE_REQUEST, USER_TOKEN)
            .getArticle();
        assertEquals(request.getTitle(), response.getTitle());
    }

    @DisplayName("[??? ?????? - ?????? ???????????? ?????? ?????? ?????? ??????]")
    @Test
    void updateNotFoundArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.update(UPDATE_ARTICLE_REQUEST, myArticle.getSlug(),
                USER_TOKEN));
    }

    @DisplayName("[??? ?????? - ???????????? ???????????? ?????? ??????")
    @Test
    void updateDuplicateSlug() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        given(articleRepository.existsArticleBySlug(any())).willReturn(true);
        assertThrows(DuplicatedSlugException.class,
            () -> articleService.update(UPDATE_ARTICLE_REQUEST, myArticle.getSlug(),
                USER_TOKEN));
    }

    @DisplayName("[??? ?????? - ????????? ???????????? ????????? ?????? ??????]")
    @Test
    void updateNotMatchAuthor() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        given(articleRepository.existsArticleBySlug(any())).willReturn(false);
        assertThrows(BadRequestException.class,
            () -> articleService.update(UPDATE_ARTICLE_REQUEST, otherArticle.getSlug(),
                USER_TOKEN));
    }

    @DisplayName("[??? ??????]")
    @Test
    void updateArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        given(articleRepository.existsArticleBySlug(any())).willReturn(false);
        UpdateArticleRequest.ArticleInfo request = UPDATE_ARTICLE_REQUEST.getArticle();
        ArticleInfo response = articleService.update(UPDATE_ARTICLE_REQUEST,
            myArticle.getSlug(), USER_TOKEN).getArticle();
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals("update-title", response.getSlug());
    }

    @DisplayName("[??? ?????? - ?????? ???????????? ?????? ?????? ?????? ??????]")
    @Test
    void deleteNotFoundArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.delete(myArticle.getSlug(), USER_TOKEN));
    }

    @DisplayName("[??? ?????? - ????????? ???????????? ????????? ?????? ??????]")
    @Test
    void deleteNotMatchAuthor() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        assertThrows(BadRequestException.class,
            () -> articleService.delete(otherArticle.getSlug(), USER_TOKEN));
    }

    @DisplayName("[??? ??????]")
    @Test
    void deleteArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        articleService.delete(myArticle.getSlug(), USER_TOKEN);
        verify(articleRepository, times(1)).delete(myArticle);
    }

    @DisplayName("[??? ?????? ??????] - ?????? ????????? ?????? ??????")
    @Test
    void findOneNotFound() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.findArticle(myArticle.getSlug(), USER_TOKEN));
    }

    @DisplayName("[??? ?????? ??????]")
    @Test
    void findOne() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        ArticleInfo response = articleService.findArticle(myArticle.getSlug(), USER_TOKEN)
            .getArticle();
        assertEquals(ARTICLE1.getDescription(), response.getDescription());
        assertEquals(ARTICLE1.getBody(), response.getBody());
    }

    @DisplayName("[??? ?????? ??????]")
    @Test
    void findAll() {
        ArticleSearchCondition condition = ArticleSearchCondition.builder()
            .tag("Springboot").author("jake").favorited("mike")
            .limit(3L).offset(0L).build();
        given(articleQueryRepository.findAll(any(ArticleSearchCondition.class)))
            .willReturn(List.of(myArticle, otherArticle));
        List<ArticleInfo> articles = articleService.findAll(USER_TOKEN, condition).getArticles();
        assertEquals(2, articles.size());
    }

    @DisplayName("[????????? ??? ??????] - ???????????? ????????? ?????? ?????? ??? ????????? ??????")
    @Test
    void findEmptyFeedArticles() {
        ArticleSearchCondition condition = ArticleSearchCondition.builder()
            .tag("Springboot").author("jake").favorited("mike")
            .limit(3L).offset(0L).build();
        given(articleQueryRepository.findFeedArticles(condition, Collections.emptySet()))
            .willReturn(Collections.emptyList());
        List<ArticleInfo> response = articleService.findFeedArticles(USER_TOKEN, condition)
            .getArticles();
        assertEquals(0, response.size());
    }

    @DisplayName("[????????? ??? ??????]")
    @Test
    void findFedArticles() {
        ArticleSearchCondition condition = ArticleSearchCondition.builder()
            .tag("Springboot").author("jake").favorited("mike")
            .limit(3L).offset(0L).build();
        given(articleQueryRepository.findFeedArticles(condition, Collections.emptySet()))
            .willReturn(List.of(myArticle, otherArticle));
        List<ArticleInfo> response = articleService.findFeedArticles(USER_TOKEN, condition)
            .getArticles();
        assertEquals(2, response.size());
    }

    @DisplayName("[??? ?????????] - ?????? ????????? ?????? ??????")
    @Test
    void favoriteNotFound() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.favoriteArticle(USER_TOKEN, myArticle.getSlug()));
    }

    @DisplayName("[??? ?????????] - ????????? ?????? ???????????? ??? ?????? ??????")
    @Test
    void favoriteMyArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        assertThrows(InvalidUserException.class,
            () -> articleService.favoriteArticle(USER_TOKEN, myArticle.getSlug()));
    }

    @DisplayName("[??? ?????????] - ?????? ????????? ??? ?????? ????????? ??? ??? ?????? ??????")
    @Test
    void favoriteDuplicate() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        assertThrows(DuplicatedFavoriteException.class,
            () -> {
                articleService.favoriteArticle(USER_TOKEN, otherArticle.getSlug());
                articleService.favoriteArticle(USER_TOKEN, otherArticle.getSlug());
            });
    }

    @DisplayName("[??? ?????????]")
    @Test
    void favorite() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        ArticleInfo response = articleService.favoriteArticle(USER_TOKEN, otherArticle.getSlug())
            .getArticle();
        assertTrue(response.isFavorite());
    }

    @DisplayName("[??? ????????? ??????] - ?????? ????????? ?????? ??????")
    @Test
    void unFavoriteNotFound() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.unFavoriteArticle(USER_TOKEN, myArticle.getSlug()));
    }

    @DisplayName("[??? ????????? ??????] - ????????? ?????? ????????? ????????? ??? ?????? ??????")
    @Test
    void unFavoriteMyArticle() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(myArticle));
        assertThrows(InvalidUserException.class,
            () -> articleService.unFavoriteArticle(USER_TOKEN, myArticle.getSlug()));
    }

    @DisplayName("[??? ????????? ??????] - ????????? ?????? ?????? ?????? ????????? ????????? ??? ?????? ??????")
    @Test
    void unFavoriteDuplicate() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        assertThrows(InvalidUnFavoriteException.class,
            () -> articleService.unFavoriteArticle(USER_TOKEN, otherArticle.getSlug()));
    }

    @DisplayName("[??? ????????? ??????]")
    @Test
    void unFavorite() {
        given(articleRepository.findArticleBySlug(any())).willReturn(Optional.of(otherArticle));
        articleService.favoriteArticle(USER_TOKEN, otherArticle.getSlug());
        ArticleInfo response = articleService.unFavoriteArticle(USER_TOKEN, otherArticle.getSlug())
            .getArticle();
        assertFalse(response.isFavorite());
    }
}