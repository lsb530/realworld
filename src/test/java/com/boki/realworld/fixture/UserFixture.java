package com.boki.realworld.fixture;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.request.LoginRequest;
import com.boki.realworld.api.user.dto.request.LoginRequest.NestedClass;
import com.boki.realworld.api.user.dto.request.RegistrationRequest;
import com.boki.realworld.api.user.dto.request.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonRootName;

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

    public static final User REGISTER_USER = User.builder()
        .username("jake")
        .email("jake@jake.com")
        .password("jakejake")
        .build();

    public static final RegistrationRequest.NestedClass REGISTER_REQUEST = RegistrationRequest.NestedClass.builder()
        .username(REGISTER_USER.getUsername())
        .email(REGISTER_USER.getEmail())
        .password(REGISTER_USER.getPassword())
        .build();

    public static final LoginRequest.NestedClass LOGIN_REQUEST = LoginRequest.NestedClass.builder()
        .email(USER1.getEmail())
        .password(USER1.getPassword())
        .build();

    public static final UpdateRequest.NestedClass UPDATE_REQUEST = UpdateRequest.NestedClass.builder()
        .email(USER2.getEmail())
        .bio(USER2.getBio())
        .image(USER2.getImage())
        .build();

}