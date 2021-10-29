package com.boki.realworld.api.user.service;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.api.user.domain.UserRepository;
import com.boki.realworld.api.user.dto.response.ProfileResponse;
import com.boki.realworld.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String username, User user) {
        User me = makePersistentOf(user);
        User other = getUserByUsername(username);
        return ProfileResponse.of(me, other);
    }

    @Transactional
    public ProfileResponse follow(String username, User user) {
        User me = makePersistentOf(user);
        User other = getUserByUsername(username);
        me.follow(other);
        return ProfileResponse.of(me, other);
    }

    @Transactional
    public ProfileResponse unfollow(String username, User user) {
        User me = makePersistentOf(user);
        User other = getUserByUsername(username);
        me.unfollow(other);
        return ProfileResponse.of(me, other);
    }

    private User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
            .orElseThrow(UserNotFoundException::new);
    }

    private User makePersistentOf(User user) {
        if (!ObjectUtils.isEmpty(user)) {
            user = userRepository.findById(user.getId()).get();
        }
        return user;
    }

}