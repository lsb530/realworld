package com.boki.realworld.api.comment.controller;

import com.boki.realworld.api.comment.dto.request.CreateCommentRequest;
import com.boki.realworld.api.comment.dto.response.MultipleCommentResponse;
import com.boki.realworld.api.comment.dto.response.SingleCommentResponse;
import com.boki.realworld.api.comment.service.CommentService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Comment"})
@RequiredArgsConstructor
@RequestMapping(value = "/api/articles/{slug}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 추가", notes = "글에 댓글을 추가한다")
    @PostMapping
    public ResponseEntity<SingleCommentResponse> addComment(
        @PathVariable(value = "slug") String slug,
        @RequestBody CreateCommentRequest request, @LoginUser UserToken userToken) {
        URI location = URI.create("/api/articles/" + slug + "/comments");
        return ResponseEntity.created(location)
            .body(commentService.create(request, slug, userToken));
    }

    @ApiOperation(value = "댓글 검색", notes = "글에 있는 댓글을 모두 검색한다(로그인시에는 Follow까지 검색)")
    @GetMapping
    public ResponseEntity<MultipleCommentResponse> findAllComments(
        @PathVariable(value = "slug") String slug, @OptionalUser UserToken userToken) {
        return ResponseEntity.ok().body(commentService.findAll(slug, userToken));
    }

    @ApiOperation(value = "댓글 삭제", notes = "글에 있는 댓글을 삭제한다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "slug") String slug,
        @PathVariable(value = "id") Long id,
        @LoginUser UserToken userToken) {
        commentService.delete(slug, id, userToken);
        return ResponseEntity.noContent().build();
    }

}