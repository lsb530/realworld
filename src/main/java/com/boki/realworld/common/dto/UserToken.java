package com.boki.realworld.common.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.boki.realworld.api.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class UserToken {

    private Long id;
    private String email;
    private String username;
    private String token;
    private String bio;
    private String image;

    public static UserToken from(User user) {
        return UserToken.builder()
            .id(user.getId())
            .email(user.getEmail())
            .username(user.getUsername())
            .token(user.getToken())
            .bio(user.getBio())
            .image(user.getImage())
            .build();
    }
}