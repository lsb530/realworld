package com.boki.realworld.api.user.service;

import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;
import static com.boki.realworld.fixture.UserFixture.USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.response.ProfileResponse.ProfileInfo;
import com.boki.realworld.api.user.exception.DuplicatedFollowException;
import com.boki.realworld.api.user.exception.InvalidUnFollowUserException;
import com.boki.realworld.api.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    private ProfileService profileService;

    @Mock
    private UserService userService;

    private User me;

    private User other;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(userService);
        me = User.builder()
            .id(1L)
            .username(USER1.getUsername())
            .email(USER1.getEmail())
            .password(USER1.getPassword())
            .build();
        other = User.builder()
            .id(2L)
            .username(USER2.getUsername())
            .email(USER2.getEmail())
            .password(USER2.getPassword())
            .build();
    }

    @DisplayName("[회원 조회] - 해당 유저명을 갖는 유저가 없으면 예외 발생")
    @Test
    void getProfileWithNotFoundUsername() {
        given(userService.getUserByUsername(any())).willThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,
            () -> profileService.getProfile(USER_TOKEN, other.getUsername()));
    }

    @DisplayName("[회원 조회]")
    @Test
    void getProfile() {
        given(userService.getUserFrom(USER_TOKEN)).willReturn(me);
        given(userService.getUserByUsername(any())).willReturn(other);
        ProfileInfo response = profileService.getProfile(USER_TOKEN, other.getUsername())
            .getProfile();
        assertEquals(response.getUsername(), other.getUsername());
    }

    @DisplayName("[팔로우] - 팔로우 한 상대를 또 팔로우하면 예외발생")
    @Test
    void followDuplicate() {
        given(userService.getUserFrom(USER_TOKEN)).willReturn(me);
        given(userService.getUserByUsername(any())).willReturn(other);
        assertThrows(DuplicatedFollowException.class,
            () -> {
                profileService.follow(USER_TOKEN, other.getUsername());
                profileService.follow(USER_TOKEN, other.getUsername());
            });
    }

    @DisplayName("[팔로우]")
    @Test
    void follow() {
        given(userService.getUserFrom(USER_TOKEN)).willReturn(me);
        given(userService.getUserByUsername(any())).willReturn(other);
        ProfileInfo response = profileService.follow(USER_TOKEN, other.getUsername())
            .getProfile();
        assertTrue(response.isFollowing());
    }

    @DisplayName("[언팔로우] - 팔로우 하지 않은 상대를 언팔로우하면 예외 발생")
    @Test
    void unfollowInvalidUnFollowUserException() {
        given(userService.getUserFrom(USER_TOKEN)).willReturn(me);
        given(userService.getUserByUsername(any())).willReturn(other);
        assertThrows(InvalidUnFollowUserException.class,
            () -> profileService.unfollow(USER_TOKEN, other.getUsername()));
    }

    @DisplayName("[언팔로우]")
    @Test
    void unfollow() {
        given(userService.getUserFrom(USER_TOKEN)).willReturn(me);
        given(userService.getUserByUsername(any())).willReturn(other);
        profileService.follow(USER_TOKEN, other.getUsername());
        ProfileInfo response = profileService.unfollow(USER_TOKEN, other.getUsername())
            .getProfile();
        assertEquals(response.getUsername(), other.getUsername());
        assertFalse(response.isFollowing());
    }
}