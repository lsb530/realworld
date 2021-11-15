package com.boki.realworld.api.user.service;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.domain.UserRepository;
import com.boki.realworld.api.user.dto.response.ProfileResponse;
import com.boki.realworld.api.user.exception.UserNotFoundException;
import com.boki.realworld.common.dto.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserService userService;

    public ProfileResponse getProfile(String username, UserToken userToken) {
        User me = userService.getUserFrom(userToken);
        User other = userService.getUserByUsername(username);
        return ProfileResponse.of(me, other);
    }

    @Transactional
    public ProfileResponse follow(String username, UserToken userToken) {
        User me = userService.getUserFrom(userToken);
        User other = userService.getUserByUsername(username);
        me.follow(other);
        return ProfileResponse.of(me, other);
    }

    @Transactional
    public ProfileResponse unfollow(String username, UserToken userToken) {
        User me = userService.getUserFrom(userToken);
        User other = userService.getUserByUsername(username);
        me.unfollow(other);
        return ProfileResponse.of(me, other);
    }

}