package com.boki.realworld.api.user.service;

import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.domain.UserRepository;
import com.boki.realworld.api.user.dto.response.ProfileResponse.ProfileInfo;
import com.boki.realworld.api.user.exception.UserNotFoundException;
import java.util.Optional;
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
    private UserRepository userRepository;

    private User me;

    private User other;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(userRepository);
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
        given(userRepository.findById(any())).willReturn(Optional.of(me));
    }

    @DisplayName("[회원 조회] - 해당 유저명을 갖는 유저가 없으면 예외 발생")
    @Test
    void getProfileWithNotFoundUsername() {
        given(userRepository.findUserByUsername(any())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
            () -> profileService.getProfile(other.getUsername(), me));
    }

    @DisplayName("[회원 조회]")
    @Test
    void getProfile() {
        given(userRepository.findUserByUsername(other.getUsername())).willReturn(
            Optional.of(other));
        ProfileInfo response2 = profileService.getProfile(other.getUsername(), me)
            .getProfileResponse();
        assertFalse(response2.isFollowing());
    }

    @DisplayName("[팔로우]")
    @Test
    void follow() {
        given(userRepository.findUserByUsername(other.getUsername())).willReturn(
            Optional.of(other));
        ProfileInfo response = profileService.follow(other.getUsername(), me)
            .getProfileResponse();
        assertTrue(response.isFollowing());
    }

    @DisplayName("[언팔로우]")
    @Test
    void unfollow() {
        given(userRepository.findUserByUsername(other.getUsername())).willReturn(
            Optional.of(other));
        me.follow(other);
        ProfileInfo response = profileService.unfollow(other.getUsername(), me)
            .getProfileResponse();
        assertFalse(response.isFollowing());
    }
}