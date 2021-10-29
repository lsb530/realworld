package com.boki.realworld.api.user.dto.response;

import com.boki.realworld.api.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    @JsonProperty("user")
    private UserInfo userResponse;

    @Getter
    @Builder
    public static class UserInfo {

        private String email;
        private String token;
        private String username;
        private String bio;
        private String image;
    }

    public static UserResponse of(User user) {
        UserInfo userInfo = UserInfo.builder()
            .email(user.getEmail())
            .token(user.getToken())
            .username(user.getUsername())
            .bio(user.getBio())
            .image(user.getImage())
            .build();
        return new UserResponse(userInfo);
    }
}