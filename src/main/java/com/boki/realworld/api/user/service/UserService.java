package com.boki.realworld.api.user.service;

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
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserResponse authenticate(LoginRequest loginRequest) {
        LoginRequest.UserInfo userInfo = loginRequest.getUser();
        User user = userRepository.findUserByEmail(userInfo.getEmail())
            .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(userInfo.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        user.setToken(tokenProvider.generateFrom(user));
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse registration(RegistrationRequest registrationRequest) {
        RegistrationRequest.UserInfo userInfo = registrationRequest.getUser();
        validatedUsername(userInfo.getUsername());
        validateEmail(userInfo.getEmail());
        User user = registrationRequest.toEntity(passwordEncoder);
        user.setToken(tokenProvider.generateFrom(user));
        return UserResponse.of(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UpdateRequest updateRequest, UserToken userToken) {
        User user = userRepository.findById(userToken.getId()).get();
        UserInfo userInfo = updateRequest.getUser();
        if (!ObjectUtils.isEmpty(userInfo.getUsername()) && !userInfo.getUsername()
            .equals(user.getUsername())) {
            validatedUsername(userInfo.getUsername());
        }
        if (!ObjectUtils.isEmpty(userInfo.getEmail()) && !userInfo.getEmail()
            .equals(user.getEmail())) {
            validateEmail(userInfo.getEmail());
        }
        String encodedPassword = null;
        if (!ObjectUtils.isEmpty(userInfo.getPassword())) {
            encodedPassword = passwordEncoder.encode(userInfo.getPassword());
        }
        user.update(userInfo.getEmail(), userInfo.getUsername(), encodedPassword,
            userInfo.getImage(), userInfo.getBio());
        user.setToken(tokenProvider.generateFrom(user));
        userRepository.save(user);
        return UserResponse.of(user);
    }

    private void validatedUsername(String username) {
        if (userRepository.existsUserByUsername(username)) {
            throw new DuplicatedUsernameException();
        }
    }

    private void validateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            throw new DuplicatedEmailException();
        }
    }

}