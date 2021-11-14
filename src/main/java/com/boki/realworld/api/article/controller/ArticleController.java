package com.boki.realworld.api.article.controller;

import com.boki.realworld.api.article.dto.condition.ArticleSearchCondition;
import com.boki.realworld.api.article.dto.request.CreateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest;
import com.boki.realworld.api.article.dto.response.MultipleArticleResponse;
import com.boki.realworld.api.article.dto.response.SingleArticleResponse;
import com.boki.realworld.api.article.service.ArticleService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<SingleArticleResponse> create(
        @Valid @RequestBody CreateArticleRequest request,
        @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(articleService.create(request, userToken));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> update(@RequestBody UpdateArticleRequest request,
        @PathVariable String slug, @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(articleService.update(request, slug, userToken));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug, @LoginUser UserToken userToken) {
        articleService.delete(slug, userToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> findArticle(
        @PathVariable(value = "slug") String slug,
        @OptionalUser UserToken userToken) {
        return ResponseEntity.ok().body(articleService.findArticle(slug, userToken));
    }

    @GetMapping
    public ResponseEntity<MultipleArticleResponse> findAll(@OptionalUser UserToken userToken,
        ArticleSearchCondition condition) {
        return ResponseEntity.ok().body(articleService.findAll(userToken, condition));
    }

    @GetMapping("/feed")
    public ResponseEntity<MultipleArticleResponse> findFeedAll(@LoginUser UserToken userToken,
        ArticleSearchCondition condition) {
        return ResponseEntity.ok().body(articleService.findFeedArticles(userToken, condition));
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticleResponse> favorite(@LoginUser UserToken userToken,
        @PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok().body(articleService.favoriteArticle(userToken, slug));
    }

    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticleResponse> unFavorite(@LoginUser UserToken userToken,
        @PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok().body(articleService.unFavoriteArticle(userToken, slug));
    }

}