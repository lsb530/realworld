package com.boki.realworld.api.user.controller;

import com.boki.realworld.api.user.dto.request.LoginRequest;
import com.boki.realworld.api.user.dto.request.RegistrationRequest;
import com.boki.realworld.api.user.dto.request.UpdateRequest;
import com.boki.realworld.api.user.dto.response.UserResponse;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/users/login")
    private ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(userService.authenticate(loginRequest));
    }

    @PostMapping(value = "/users")
    private ResponseEntity<UserResponse> register(
        @Valid @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok().body(userService.registration(registrationRequest));
    }

    @GetMapping("/user")
    private ResponseEntity<UserResponse> me(@LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(UserResponse.of(userToken));
    }

    @GetMapping("/user/v2")
    private ResponseEntity<UserResponse> meNonResolver(@RequestAttribute UserToken user) {
        return ResponseEntity.ok().body(UserResponse.of(user));
    }

    @PutMapping("/user")
    private ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateRequest updateRequest,
        @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(userService.update(updateRequest, userToken));
    }
}