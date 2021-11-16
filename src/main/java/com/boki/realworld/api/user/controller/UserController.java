package com.boki.realworld.api.user.controller;

import com.boki.realworld.api.user.dto.request.LoginRequest;
import com.boki.realworld.api.user.dto.request.RegistrationRequest;
import com.boki.realworld.api.user.dto.request.UpdateRequest;
import com.boki.realworld.api.user.dto.response.UserResponse;
import com.boki.realworld.api.user.service.UserService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
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

@Api(tags = {"User"})
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {

    private final UserService userService;


    @ApiOperation(value = "로그인", notes = "이메일과 비밀번호로 로그인한다하고 토큰값을 받는다")
    @PostMapping(value = "/users/login")
    private ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(userService.authenticate(request));
    }

    @ApiOperation(value = "회원가입", notes = "유저명, 이메일, 비밀번호로 회원가입한다")
    @PostMapping(value = "/users")
    private ResponseEntity<UserResponse> register(
        @Valid @RequestBody RegistrationRequest request) {
        URI location = URI.create("/api/profiles/" + request.getUser().getUsername());
        return ResponseEntity.created(location).body(userService.registration(request));
    }

    @ApiOperation(value = "로그인(리졸버)", notes = "헤더의 토큰으로 로그인한다")
    @GetMapping("/user")
    private ResponseEntity<UserResponse> me(@LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(UserResponse.of(userToken));
    }

    @ApiOperation(value = "로그인(인터셉터)", notes = "헤더의 토큰으로 로그인한다")
    @GetMapping("/user/v2")
    private ResponseEntity<UserResponse> meNonResolver(
        @RequestAttribute("LoginUser") UserToken userToken) {
        return ResponseEntity.ok().body(UserResponse.of(userToken));
    }

    @ApiOperation(value = "유저 정보수정", notes = "유저의 정보를 수정한다")
    @PutMapping("/user")
    private ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateRequest request,
        @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(userService.update(request, userToken));
    }
}