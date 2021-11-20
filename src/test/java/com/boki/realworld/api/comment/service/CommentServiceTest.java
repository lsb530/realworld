package com.boki.realworld.api.comment.service;

import static com.boki.realworld.fixture.ArticleFixture.ARTICLE1;
import static com.boki.realworld.fixture.CommentFixture.COMMENT1;
import static com.boki.realworld.fixture.CommentFixture.COMMENT2;
import static com.boki.realworld.fixture.CommentFixture.CREATE_COMMENT_REQUEST;
import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.boki.realworld.api.article.domain.Article;
import com.boki.realworld.api.article.exception.ArticleNotFoundException;
import com.boki.realworld.api.article.service.ArticleService;
import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.comment.domain.CommentRepository;
import com.boki.realworld.api.comment.dto.request.CreateCommentRequest;
import com.boki.realworld.api.comment.dto.response.SingleCommentResponse.CommentInfo;
import com.boki.realworld.api.comment.exception.CommentNotFoundException;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.exception.BadRequestException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleService articleService;

    @Mock
    private UserService userService;

    private User me;


    private Article article;

    private Comment myComment;

    private Comment otherComment;

    @BeforeEach
    void setUp() {
        me = USER1;
        myComment = COMMENT1;
        otherComment = COMMENT2;
        article = ARTICLE1;
        given(userService.getUserFrom(any())).willReturn(me);
    }

    @DisplayName("[댓글 생성]")
    @Test
    void createComment() {
        CreateCommentRequest request = CREATE_COMMENT_REQUEST;
        given(articleService.getArticleOf(any())).willReturn(article);
        given(commentRepository.save(any(Comment.class))).willReturn(myComment);
        CommentInfo response = commentService.create(request, article.getSlug(), USER_TOKEN)
            .getComment();
        assertEquals(response.getBody(), request.getComment().getBody());
    }

    @DisplayName("[댓글 전체 조회]")
    @Test
    void findAllComments() {
        given(commentRepository.findCommentByArticle_Slug(any()))
            .willReturn(List.of(COMMENT1, COMMENT2));
        List<CommentInfo> response = commentService.findAll(article.getSlug(), USER_TOKEN)
            .getComments();
        assertEquals(2, response.size());
    }

    @DisplayName("[댓글 삭제] - 댓글이 달린 글이 존재하지 않는 경우 예외 발생")
    @Test
    void deleteWithNonArticle() {
        given(articleService.getArticleOf(any())).willThrow(ArticleNotFoundException.class);
        assertThrows(ArticleNotFoundException.class,
            () -> commentService.delete(article.getSlug(), article.getId(), USER_TOKEN));
    }

    @DisplayName("[댓글 삭제] - 댓글이 존재하지 않는 경우 예외 발생")
    @Test
    void deleteNotFound() {
        given(articleService.getArticleOf(any())).willReturn(article);
        given(commentRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class,
            () -> commentService.delete(article.getSlug(), article.getId(), USER_TOKEN));
    }

    @DisplayName("[댓글 삭제] - 댓글의 작성자가 본인이 아닌 경우 예외 발생")
    @Test
    void deleteByNotAuthor() {
        given(articleService.getArticleOf(any())).willReturn(article);
        given(commentRepository.findById(any())).willReturn(Optional.of(otherComment));
        assertThrows(BadRequestException.class,
            () -> commentService.delete(article.getSlug(), otherComment.getId(), USER_TOKEN));
    }

    @DisplayName("[댓글 삭제]")
    @Test
    void deleteComment() {
        given(articleService.getArticleOf(any())).willReturn(article);
        given(commentRepository.findById(any())).willReturn(Optional.of(myComment));

        commentService.delete(article.getSlug(), myComment.getId(), USER_TOKEN);

        verify(commentRepository).delete(myComment);
    }
}