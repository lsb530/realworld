package com.boki.realworld.api.user.controller;

import com.boki.realworld.api.user.dto.response.ProfileResponse;
import com.boki.realworld.api.user.service.ProfileService;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Profile"})
@RequiredArgsConstructor
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @ApiOperation(value = "프로필 조회(리졸버)", notes = "유저 정보와 follow 상태까지 조회한다")
    @GetMapping("/{username}")
    private ResponseEntity<ProfileResponse> getProfile(
        @PathVariable("username") String username, @OptionalUser UserToken userToken) {
        return ResponseEntity.ok().body(profileService.getProfile(username, userToken));
    }

    @ApiOperation(value = "프로필 조회(인터셉터)", notes = "유저 정보와 follow 상태까지 조회한다")
    @GetMapping("/{username}/v2")
    private ResponseEntity<ProfileResponse> getProfileNonResolver(
        @PathVariable("username") String username,
        @Nullable @RequestAttribute("OptionalUser") UserToken userToken) {
        return ResponseEntity.ok().body(profileService.getProfile(username, userToken));
    }

    @ApiOperation(value = "팔로우", notes = "유저를 팔로우한다")
    @PostMapping("/{username}/follow")
    private ResponseEntity<ProfileResponse> follow(
        @PathVariable("username") String username, @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(profileService.follow(username, userToken));
    }

    @ApiOperation(value = "언팔로우", notes = "유저를 언팔로우한다")
    @DeleteMapping("/{username}/follow")
    private ResponseEntity<ProfileResponse> unfollow(
        @PathVariable("username") String username, @LoginUser UserToken userToken) {
        return ResponseEntity.ok().body(profileService.unfollow(username, userToken));
    }
}