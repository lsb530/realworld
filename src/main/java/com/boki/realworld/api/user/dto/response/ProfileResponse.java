package com.boki.realworld.api.user.dto.response;

import com.boki.realworld.api.user.domain.User;
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

    private ProfileInfo profile;

    public static ProfileResponse of(User me, User other) {
        return new ProfileResponse(ProfileInfo.of(me, other));
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProfileInfo {

        private String username;
        private String bio;
        private String image;
        private boolean following;

        public static ProfileInfo of(User me, User other) {
            return ProfileInfo.builder()
                .username(other.getUsername())
                .bio(other.getBio())
                .image(other.getImage())
                .following(!ObjectUtils.isEmpty(me) && me.isFollow(other))
                .build();
        }
    }
}