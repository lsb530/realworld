package com.boki.realworld.api.comment.controller;

import com.boki.realworld.api.comment.dto.request.CreateCommentRequest;
import com.boki.realworld.api.comment.dto.response.MultipleCommentResponse;
import com.boki.realworld.api.comment.dto.response.SingleCommentResponse;
import com.boki.realworld.api.comment.service.CommentService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
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

@RequiredArgsConstructor
@RequestMapping(value = "/api/articles/{slug}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SingleCommentResponse> addComment(
        @PathVariable(value = "slug") String slug,
        @RequestBody CreateCommentRequest request, @LoginUser UserToken userToken) {
        URI location = URI.create("/api/articles/" + slug + "/comments");
        return ResponseEntity.created(location)
            .body(commentService.create(request, slug, userToken));
    }

    @GetMapping
    public ResponseEntity<MultipleCommentResponse> findAllComments(
        @PathVariable(value = "slug") String slug, @OptionalUser UserToken userToken) {
        return ResponseEntity.ok().body(commentService.findAll(slug, userToken));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable(value = "slug") String slug,
        @PathVariable(value = "id") Long id,
        @LoginUser UserToken userToken) {
        commentService.delete(slug, id, userToken);
        return ResponseEntity.noContent().build();
    }

}