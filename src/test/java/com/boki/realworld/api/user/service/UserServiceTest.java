package com.boki.realworld.api.user.service;

import static com.boki.realworld.fixture.UserFixture.USER1;
import static com.boki.realworld.fixture.UserFixture.USER2;
import static com.boki.realworld.fixture.UserFixture.USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.domain.UserRepository;
import com.boki.realworld.api.user.dto.request.LoginRequest;
import com.boki.realworld.api.user.dto.request.RegistrationRequest;
import com.boki.realworld.api.user.dto.request.UpdateRequest;
import com.boki.realworld.api.user.dto.request.UpdateRequest.UserInfo;
import com.boki.realworld.api.user.dto.response.UserResponse;
import com.boki.realworld.api.user.exception.DuplicatedEmailException;
import com.boki.realworld.api.user.exception.DuplicatedUsernameException;
import com.boki.realworld.api.user.exception.UserNotFoundException;
import com.boki.realworld.api.user.exception.WrongPasswordException;
import com.boki.realworld.config.TokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    private User user;

    private LoginRequest.UserInfo loginUserdata;

    private RegistrationRequest.UserInfo registrationUserData;

    private UpdateRequest.UserInfo updateUserData;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, tokenProvider, passwordEncoder);
        user = User.builder()
            .email(USER1.getEmail()).username(USER1.getUsername())
            .password(USER1.getPassword()).build();
        loginUserdata = LoginRequest.UserInfo.builder()
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
        registrationUserData = RegistrationRequest.UserInfo.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
        updateUserData = UserInfo.builder()
            .email(USER2.getEmail())
            .username(USER2.getUsername())
            .password(user.getPassword())
            .bio("I like to skateboard")
            .image("https://i.stack.imgur.com/xHWG8.jpg")
            .build();
    }

    @DisplayName("[로그인] - 비밀번호가 일치하지 않으면 예외 발생")
    @Test
    void authenticateWithWrongPassword() {
        LoginRequest loginRequest = new LoginRequest(loginUserdata);

        given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        assertThrows(WrongPasswordException.class, () -> userService.authenticate(loginRequest));
    }

    @DisplayName("[로그인] - 해당 이메일이 없으면 예외 발생")
    @Test
    void authenticateWithNotFoundEmail() {
        LoginRequest loginRequest = new LoginRequest(loginUserdata);

        given(userRepository.findUserByEmail(anyString())).willReturn(Optional.empty());
//        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        assertThrows(UserNotFoundException.class, () -> userService.authenticate(loginRequest));
    }

    @DisplayName("[로그인]")
    @Test
    void authenticate() {
        LoginRequest loginRequest = new LoginRequest(loginUserdata);

        given(userRepository.findUserByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(tokenProvider.generateFrom(user)).willReturn("abc.def.ghi");

        com.boki.realworld.api.user.dto.response.UserResponse.UserInfo response = userService.authenticate(
            loginRequest).getUser();

        assertAll(
            () -> assertEquals(response.getEmail(), user.getEmail()),
            () -> assertEquals(response.getUsername(), user.getUsername()),
            () -> assertEquals("abc.def.ghi", response.getToken())
        );
    }

    @DisplayName("[회원가입] - 해당 이메일이 중복이라면 예외 발생")
    @Test
    void registrationWithDuplicatedEmail() {
        RegistrationRequest registrationRequest = new RegistrationRequest(registrationUserData);

        given(userRepository.existsUserByEmail(anyString())).willReturn(true);

        assertThrows(DuplicatedEmailException.class,
            () -> userService.registration(registrationRequest));
    }

    @DisplayName("[회원가입] - 해당 유저명이 중복이라면 예외 발생")
    @Test
    void registrationWithDuplicatedUsername() {
        RegistrationRequest registrationRequest = new RegistrationRequest(registrationUserData);

        given(userRepository.existsUserByUsername(anyString())).willReturn(true);

        assertThrows(DuplicatedUsernameException.class,
            () -> userService.registration(registrationRequest));
    }

    @DisplayName("[회원가입]")
    @Test
    void registration() {
        RegistrationRequest registrationRequest = new RegistrationRequest(registrationUserData);

        given(passwordEncoder.encode(anyString())).willReturn("password");
        given(userRepository.existsUserByUsername(anyString())).willReturn(false);
        given(userRepository.existsUserByEmail(anyString())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        com.boki.realworld.api.user.dto.response.UserResponse.UserInfo response = userService.registration(
            registrationRequest).getUser();
        assertAll(
            () -> assertEquals(response.getUsername(), user.getUsername()),
            () -> assertEquals(response.getEmail(), user.getEmail())
        );
    }

    @DisplayName("[회원정보 수정] - 해당 이메일이 중복이라면 예외 발생")
    @Test
    void updateWithDuplicatedEmail() {
        UpdateRequest updateRequest = new UpdateRequest(updateUserData);

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(userRepository.existsUserByEmail(any())).willReturn(true);
        given(userRepository.existsUserByUsername(any())).willReturn(false);

        assertThrows(DuplicatedEmailException.class,
            () -> userService.update(updateRequest, USER_TOKEN));
    }

    @DisplayName("[회원정보 수정] - 해당 유저명이 중복이라면 예외 발생")
    @Test
    void updateWithDuplicatedUsername() {
        UpdateRequest updateRequest = new UpdateRequest(updateUserData);

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(userRepository.existsUserByUsername(any())).willReturn(true); // null 처리때문에 any()

        assertThrows(DuplicatedUsernameException.class,
            () -> userService.update(updateRequest, USER_TOKEN));
    }

    @DisplayName("[회원정보 수정]")
    @Test
    void update() {
        UpdateRequest updateRequest = new UpdateRequest(updateUserData);

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(userRepository.existsUserByUsername(any())).willReturn(false); // null 처리때문에 any()
        given(userRepository.existsUserByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn("password");
        given(tokenProvider.generateFrom(user)).willReturn("abc.def.ghi");
//        given(tokenProvider.generate(any())).willReturn("abc.def.ghi");

        UserResponse.UserInfo response = userService.update(
            updateRequest, USER_TOKEN).getUser();

        assertAll(
            () -> assertEquals(response.getEmail(), USER2.getEmail()),
            () -> assertEquals(response.getUsername(), USER2.getUsername()),
            () -> assertEquals(response.getToken(), "abc.def.ghi"),
            () -> assertEquals(response.getBio(), "I like to skateboard"),
            () -> assertEquals(response.getImage(), "https://i.stack.imgur.com/xHWG8.jpg")
        );
    }

}