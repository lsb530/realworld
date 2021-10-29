package com.boki.realworld.api.user.controller;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.dto.response.ProfileResponse;
import com.boki.realworld.api.user.service.ProfileService;
import com.boki.realworld.resolver.LoginUser;
import com.boki.realworld.resolver.OptionalUser;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    private ResponseEntity<ProfileResponse> getProfile(
        @PathVariable("username") String username, @OptionalUser User user) {
        return ResponseEntity.ok().body(profileService.getProfile(username, user));
    }

    @GetMapping("/{username}/v2")
    private ResponseEntity<ProfileResponse> getProfileNonResolver(
        @PathVariable("username") String username, HttpServletRequest request) {
        Object user = request.getAttribute("user");
        if (!ObjectUtils.isEmpty(user)) {
            return ResponseEntity.ok().body(profileService.getProfile(username, (User) user));
        } else {
            return ResponseEntity.ok().body(profileService.getProfile(username, null));
        }
    }

    @PostMapping("/{username}/follow")
    private ResponseEntity<ProfileResponse> follow(
        @PathVariable("username") String username, @LoginUser User user) {
        return ResponseEntity.ok().body(profileService.follow(username, user));
    }

    @DeleteMapping("/{username}/follow")
    private ResponseEntity<ProfileResponse> unfollow(
        @PathVariable("username") String username, @LoginUser User user) {
        return ResponseEntity.ok().body(profileService.unfollow(username, user));
    }
}