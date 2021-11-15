package com.boki.realworld.api.comment.dto.response;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.boki.realworld.api.comment.domain.Comment;
import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.response.ProfileResponse.ProfileInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleCommentResponse {

    private CommentInfo comment;

    public static SingleCommentResponse of(Comment comment, User me) {
        return new SingleCommentResponse(CommentInfo.of(comment, me));
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CommentInfo {

        private Long id;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;
        private String body;
        private ProfileInfo author;

        public static CommentInfo of(Comment comment, User me) {
            return CommentInfo.builder()
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .body(comment.getBody())
                .author(ProfileInfo.of(me, comment.getAuthor()))
                .build();
        }

    }
}