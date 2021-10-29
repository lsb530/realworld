package com.boki.realworld.api.user.dto.response;

import com.boki.realworld.api.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponse {

    @JsonProperty("profile")
    private ProfileInfo profileResponse;

    @Getter
    @Builder
    public static class ProfileInfo {

        private final String username;
        private final String bio;
        private final String image;
        private final boolean following;
    }

    public static ProfileResponse of(User me, User other) {
        ProfileInfo profileInfo = ProfileInfo.builder()
            .username(other.getUsername())
            .bio(other.getBio())
            .image(other.getImage())
            .following(!ObjectUtils.isEmpty(me) && me.isFollow(other))
            .build();
        return new ProfileResponse(profileInfo);
    }
}