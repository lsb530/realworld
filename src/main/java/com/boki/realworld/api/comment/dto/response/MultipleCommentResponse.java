package com.boki.realworld.api.comment.dto.response;

import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.comment.dto.response.SingleCommentResponse.CommentInfo;
import com.boki.realworld.api.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleCommentResponse {

    private List<CommentInfo> comments;

    public static MultipleCommentResponse of(List<Comment> comments, User me) {
        return new MultipleCommentResponse(comments.stream()
            .map(comment -> CommentInfo.of(comment, me)).collect(Collectors.toList()));
    }
}