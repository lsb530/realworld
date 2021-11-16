package com.boki.realworld.api.article.controller;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.dto.condition.ArticleSearchCondition;
import com.boki.realworld.api.article.dto.request.CreateArticleRequest;
import com.boki.realworld.api.article.dto.request.UpdateArticleRequest;
import com.boki.realworld.api.article.dto.response.MultipleArticleResponse;
import com.boki.realworld.api.article.dto.response.SingleArticleResponse;
import com.boki.realworld.api.article.service.ArticleService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
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

@Api(tags = {"Article"})
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "글 작성", notes = "글을 작성한다")
    @PostMapping
    public ResponseEntity<SingleArticleResponse> create(
        @Valid @RequestBody CreateArticleRequest request, @LoginUser UserToken userToken) {
        String slug = Article.toSlugFrom(request.getArticle().getTitle());
        URI location = URI.create("/api/articles/" + slug);
        return ResponseEntity.created(location).body(articleService.create(request, userToken));
    }

    @ApiOperation(value = "글 수정", notes = "글을 수정한다")
    @PutMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> update(@RequestBody UpdateArticleRequest request,
        @PathVariable String slug, @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(articleService.update(request, slug, userToken));
    }

    @ApiOperation(value = "글 삭제", notes = "글을 삭제한다")
    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> delete(@PathVariable String slug, @LoginUser UserToken userToken) {
        articleService.delete(slug, userToken);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "글 검색", notes = "하나의 글을 검색한다")
    @GetMapping("/{slug}")
    public ResponseEntity<SingleArticleResponse> findArticle(
        @PathVariable(value = "slug") String slug,
        @OptionalUser UserToken userToken) {
        return ResponseEntity.ok().body(articleService.findArticle(slug, userToken));
    }

    @ApiOperation(value = "글 전체 검색", notes = "여러개의 글을 검색한다")
    @GetMapping
    public ResponseEntity<MultipleArticleResponse> findAll(@OptionalUser UserToken userToken,
        ArticleSearchCondition condition) {
        return ResponseEntity.ok().body(articleService.findAll(userToken, condition));
    }

    @ApiOperation(value = "팔로잉한 유저의 글 전체 검색", notes = "자신이 팔로잉한 유저의 글들을 검색한다")
    @GetMapping("/feed")
    public ResponseEntity<MultipleArticleResponse> findFeedAll(@LoginUser UserToken userToken,
        ArticleSearchCondition condition) {
        return ResponseEntity.ok().body(articleService.findFeedArticles(userToken, condition));
    }

    @ApiOperation(value = "글 좋아요", notes = "글을 좋아요하는 기능")
    @PostMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticleResponse> favorite(@LoginUser UserToken userToken,
        @PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok().body(articleService.favoriteArticle(userToken, slug));
    }

    @ApiOperation(value = "글 싫어요", notes = "글을 싫어요하는 기능")
    @DeleteMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticleResponse> unFavorite(@LoginUser UserToken userToken,
        @PathVariable(value = "slug") String slug) {
        return ResponseEntity.ok().body(articleService.unFavoriteArticle(userToken, slug));
    }

}