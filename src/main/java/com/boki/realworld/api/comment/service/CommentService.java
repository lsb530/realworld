package com.boki.realworld.api.comment.service;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.service.ArticleService;
import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.comment.domain.CommentRepository;
import com.boki.realworld.api.comment.dto.request.CreateCommentRequest;
import com.boki.realworld.api.comment.dto.response.MultipleCommentResponse;
import com.boki.realworld.api.comment.dto.response.SingleCommentResponse;
import com.boki.realworld.api.comment.exception.CommentNotFoundException;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.common.exception.BadRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;

    @Transactional
    public SingleCommentResponse create(CreateCommentRequest request, String slug,
        UserToken userToken) {
        User author = userService.getUserFrom(userToken);
        Article article = articleService.getArticleOf(slug);
        Comment comment = request.toEntity(article, author);
        return SingleCommentResponse.of(commentRepository.save(comment), author);
    }

    public MultipleCommentResponse findAll(String slug, UserToken userToken) {
        User author = userService.getUserFrom(userToken);
        List<Comment> commentByArticleSlug = commentRepository.findCommentByArticle_Slug(slug);
        return MultipleCommentResponse.of(commentByArticleSlug, author);
    }

    @Transactional
    public void delete(String slug, Long id, UserToken userToken) {
        User user = userService.getUserFrom(userToken);
        Article article = articleService.getArticleOf(slug);
        Comment comment = findCommentBy(id);
        verifyArticle(comment, article);
        verifyAuthor(comment, user);
        commentRepository.delete(comment);
    }

    private Comment findCommentBy(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
    }

    private void verifyArticle(Comment comment, Article article) {
        if (comment.getArticle() != article) {
            throw new BadRequestException("That article haven't this comment");
        }
    }

    private void verifyAuthor(Comment comment, User user) {
        if (comment.getAuthor() != user) {
            throw new BadRequestException("You are not author");
        }
    }
}