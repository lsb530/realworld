package com.boki.realworld.api.user.domain;

import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.boki.realworld.api.user.exception.DuplicatedFollowException;
import com.boki.realworld.api.user.exception.InvalidUnFollowUserException;
import com.boki.realworld.api.user.exception.InvalidUserException;
import com.boki.realworld.common.exception.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private User me;
    private User other;

    @BeforeEach
    void setUp() {
        me = User.builder()
            .email(USER1.getEmail())
            .username(USER1.getUsername())
            .password(USER1.getPassword())
            .build();
        other = User.builder()
            .email(USER2.getEmail())
            .username(USER2.getUsername())
            .password(USER2.getPassword())
            .build();
    }

//    @DisplayName("[Dynamic] 유저 생성 예외 처리 테스트")
//    @TestFactory
//    Stream<DynamicTest> createUserTest() {
//        return Stream.of(
//            dynamicTest("이메일이 없으면 예외 발생", () -> {
//                assertThrows(IllegalParameterException.class, () ->
//                    User.builder()
//                        .username(USER1.getUsername())
//                        .password(USER1.getPassword())
//                        .build());
//            }),
//            dynamicTest("유저명이 없으면 예외 발생", () -> {
//                assertThrows(IllegalParameterException.class, () ->
//                    User.builder()
//                        .email(USER1.getEmail())
//                        .password(USER1.getPassword())
//                        .build());
//            }),
//            dynamicTest("비밀번호가 없으면 예외 발생", () -> {
//                assertThrows(IllegalParameterException.class, () ->
//                    User.builder()
//                        .username(USER1.getUsername())
//                        .email(USER1.getEmail())
//                        .build());
//            }),
//            dynamicTest("유저 생성 확인", () -> {
//                User createdUser = User.builder()
//                    .email(USER1.getEmail())
//                    .username(USER1.getUsername())
//                    .password(USER1.getPassword())
//                    .build();
//                assertAll(
//                    () -> assertEquals(USER1.getEmail(), createdUser.getEmail(),
//                        "생성된 유저의 이메일이 다릅니다"),
//                    () -> assertEquals(USER1.getUsername(), createdUser.getUsername(),
//                        "생성된 유저의 이름이 다릅니다"),
//                    () -> assertEquals(USER1.getPassword(), createdUser.getPassword(),
//                        "생성된 유저의 비밀번호가 다릅니다")
//                );
//            })
//        );
//    }

    @DisplayName("[유저 생성] - 이메일이 없으면 예외 발생")
    @Test
    void createUserWithoutEmail() {
        assertThrows(IllegalParameterException.class,
            () -> User.builder().username(me.getUsername()).password(me.getPassword()).build()
            , "유저 생성시 이메일 필드가 있어야 한다"
        );
    }

    @DisplayName("[유저 생성] - 유저명이 없으면 예외 발생")
    @Test
    void createUserWithoutUsername() {
        assertThrows(IllegalParameterException.class, () ->
            User.builder().email(USER1.getEmail()).password(USER1.getPassword()).build());
    }

    @DisplayName("[유저 생성] - 비밀번호가 없으면 예외 발생")
    @Test
    void createUserWithoutPassword() {
        assertThrows(IllegalParameterException.class, () ->
            User.builder().username(USER1.getUsername()).email(USER1.getEmail()).build());
    }

    @DisplayName("[유저 생성]")
    @Test
    void createUser() {
        User user = User.builder().email(me.getEmail()).username(me.getUsername())
            .password(me.getPassword()).build();
        assertAll(
            () -> assertEquals(me.getEmail(), user.getEmail()),
            () -> assertEquals(me.getUsername(), user.getUsername()),
            () -> assertEquals(me.getPassword(), user.getPassword())
        );
    }

    @DisplayName("[정보 수정]")
    @Test
    public void update() {
        String email = "change@change.change";
        String password = "changechange";
        String image = "https://image.com";
        // when
        me.update(email, null, password, image, null);
        // then
        assertAll(
            () -> assertEquals(email, me.getEmail()),
            () -> assertEquals(USER1.getUsername(), me.getUsername()),
            () -> assertNotEquals(USER1.getPassword(), me.getPassword()),
            () -> assertEquals(image, me.getImage()),
            () -> assertNull(me.getBio())
        );
    }

    @DisplayName("[팔로우] - 자기 자신을 팔로우하거나 언팔로우하면 예외 발생")
    @Test
    void followValidMe() {
        assertAll(
            () -> assertThrows(InvalidUserException.class, () -> me.follow(me),
                "자기 자신은 팔로우 할 수 없다"),
            () -> assertThrows(InvalidUserException.class, () -> me.unfollow(me),
                "자기 자신은 언팔로우 될 수 없다")
        );
    }

    @DisplayName("[팔로우] - 이미 팔로우된 상대면 예외 발생")
    @Test
    void followTwice() {
        me.follow(other);
        assertThrows(DuplicatedFollowException.class, () -> me.follow(other));
    }

    @DisplayName("[팔로우]")
    @Test
    void follow() {
        me.follow(other);
        assertTrue(me.getFollowList().contains(other));
    }

    @DisplayName("[언팔로우] - 언팔로우하지 않은 상대를 언팔로우 할때 예외 발생")
    @Test
    void unfollowTwice() {
        assertThrows(InvalidUnFollowUserException.class, () -> me.unfollow(other));
    }

    @DisplayName("[언팔로우]")
    @Test
    void unfollow() {
        me.follow(other);
        assertTrue(me.getFollowList().contains(other));
        me.unfollow(other);
        assertEquals(0, me.getFollowList().size());
    }

}