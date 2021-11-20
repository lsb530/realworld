package com.boki.realworld.fixture;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.request.UpdateRequest.UserInfo;
import com.boki.realworld.common.dto.UserToken;

public class UserFixture {

    public static final User USER1 = User.builder()
        .id(1L)
        .email("jake@jake.com")
        .username("jake")
        .password("jakejake")
        .token("abc.def.ghi")
        .bio("hello")
        .image("https://i.stack.imgur.com/xHWG8.jpg")
        .build();

    public static final User USER2 = User.builder()
        .id(2L)
        .email("mike@mike.com")
        .username("mike")
        .password("mikemike")
        .token("abc.def.ghi")
        .bio("hello")
        .image("https://i.stack.imgur.com/xHWG8.jpg")
        .build();

    public static final User USER3 = User.builder()
        .id(3L)
        .email("bike@bike.com")
        .username("bike")
        .password("bikebike")
        .token("abc.def.ghi")
        .bio("bye")
        .image(null)
        .build();

    public static final User REGISTER_USER = User.builder()
        .username("jake")
        .email("jake@jake.com")
        .password("jakejake")
        .build();

    public static final UserToken USER_TOKEN = UserToken.from(USER1);

    public static final com.boki.realworld.api.user.dto.request.RegistrationRequest.UserInfo REGISTER_REQUEST = com.boki.realworld.api.user.dto.request.RegistrationRequest.UserInfo.builder()
        .username(REGISTER_USER.getUsername())
        .email(REGISTER_USER.getEmail())
        .password(REGISTER_USER.getPassword())
        .build();

    public static final com.boki.realworld.api.user.dto.request.LoginRequest.UserInfo LOGIN_REQUEST = com.boki.realworld.api.user.dto.request.LoginRequest.UserInfo.builder()
        .email(USER1.getEmail())
        .password(USER1.getPassword())
        .build();

    public static final UserInfo UPDATE_REQUEST = UserInfo.builder()
        .email(USER2.getEmail())
        .bio(USER2.getBio())
        .image(USER2.getImage())
        .build();

}