package com.boki.realworld.api.user.dto.response;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.common.dto.UserToken;
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

    private UserInfo user;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static UserResponse of(UserToken userToken) {
        UserInfo userInfo = UserInfo.builder()
            .email(userToken.getEmail())
            .username(userToken.getUsername())
            .token(userToken.getToken())
            .bio(userToken.getBio())
            .image(userToken.getImage())
            .build();
        return new UserResponse(userInfo);
    }
}