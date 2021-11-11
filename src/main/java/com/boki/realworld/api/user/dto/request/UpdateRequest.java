package com.boki.realworld.api.user.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateRequest {

    @Valid
    private UserInfo user;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfo {

        @Email(message = "email address is not in a valid format.")
        private String email;

        @Length(min = 2, max = 20, message = "username should")
        private String username;

        @Length(min = 8, max = 20, message = "password should be between 8-20")
        private String password;

        @URL
        private String image;

        private String bio;
    }
}