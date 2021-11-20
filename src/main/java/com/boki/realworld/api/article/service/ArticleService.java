package com.boki.realworld.api.article.service;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.domain.ArticleQueryRepository;
import com.boki.realworld.api.article.domain.ArticleRepository;
import com.boki.realworld.api.article.dto.condition.ArticleSearchCondition;
import com.boki.realworld.api.article.dto.request.CreateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest.ArticleInfo;
import com.boki.realworld.api.article.dto.response.MultipleArticleResponse;
import com.boki.realworld.api.article.dto.response.SingleArticleResponse;
import com.boki.realworld.api.article.exception.ArticleNotFoundException;
import com.boki.realworld.api.article.exception.DuplicatedSlugException;
import com.boki.realworld.api.tag.domain.Tag;
import com.boki.realworld.api.tag.service.TagService;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.common.exception.BadRequestException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleQueryRepository articleQueryRepository;
    private final UserService userService;
    private final TagService tagService;

    @Transactional
    public SingleArticleResponse create(CreateArticleRequest request, UserToken userToken) {
        User author = userService.getUserFrom(userToken);
        Article article = request.toEntity(author);
        validateSlug(article.getSlug());
        if (Optional.ofNullable(request.getArticle().getTagList()).isPresent()) {
            List<Tag> tags = tagService.saveAndFindAll(request.getArticle().getTagList());
            tags.forEach(article::addTag);
        }
        return SingleArticleResponse.of(articleRepository.save(article), author);
    }

    @Transactional
    public SingleArticleResponse update(UpdateArticleRequest request, String slug,
        UserToken userToken) {
        User user = userService.getUserFrom(userToken);
        Article article = getArticleOf(slug);
        Optional.ofNullable(request.getArticle().getTitle())
            .ifPresent(title -> validateSlug(Article.toSlugFrom(title)));
        verifyAuthor(article, user);
        ArticleInfo updateData = request.getArticle();
        article.update(updateData.getTitle(), updateData.getDescription(), updateData.getBody());
        articleRepository.flush();
        return SingleArticleResponse.of(article, user);
    }

    @Transactional
    public void delete(String slug, UserToken userToken) {
        User user = userService.getUserFrom(userToken);
        Article article = getArticleOf(slug);
        verifyAuthor(article, user);
        articleRepository.delete(article);
    }

    public SingleArticleResponse findArticle(String slug, UserToken userToken) {
        User user = userService.getUserFrom(userToken);
        Article article = getArticleOf(slug);
        return SingleArticleResponse.of(article, user);
    }

    public MultipleArticleResponse findAll(UserToken userToken, ArticleSearchCondition condition) {
        User user = userService.getUserFrom(userToken);
        List<Article> articles = articleQueryRepository.findAll(condition);
        return MultipleArticleResponse.of(articles, user);
    }

    public MultipleArticleResponse findFeedArticles(UserToken userToken,
        ArticleSearchCondition condition) {
        User user = userService.getUserFrom(userToken);
        Set<User> followList = Objects.requireNonNull(user).getFollowList();
        return MultipleArticleResponse.of(
            articleQueryRepository.findFeedArticles(condition, followList), user);
    }

    @Transactional
    public SingleArticleResponse favoriteArticle(UserToken userToken, String slug) {
        User user = userService.getUserFrom(userToken);
        Article article = getArticleOf(slug);
        article.favorite(user);
        return SingleArticleResponse.of(article, user);
    }

    @Transactional
    public SingleArticleResponse unFavoriteArticle(UserToken userToken, String slug) {
        User user = userService.getUserFrom(userToken);
        Article article = getArticleOf(slug);
        article.unFavorite(user);
        return SingleArticleResponse.of(article, user);
    }

    private void validateSlug(String slug) {
        if (articleRepository.existsArticleBySlug(slug)) {
            throw new DuplicatedSlugException();
        }
    }

    private void verifyAuthor(Article article, User user) {
        if (article.getAuthor() != user) {
            throw new BadRequestException("You are not author");
        }
    }

    public Article getArticleOf(String slug) {
        return articleRepository.findArticleBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
    }
}