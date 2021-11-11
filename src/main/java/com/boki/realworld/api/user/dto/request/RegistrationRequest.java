package com.boki.realworld.api.user.dto.request;

import com.boki.realworld.api.user.domain.User;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegistrationRequest {

    @Valid
    private UserInfo user;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(passwordEncoder.encode(user.getPassword()))
            .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserInfo {

        @NotBlank(message = "username cannot be empty.")
        @Length(min = 2, max = 20, message = "username should")
        private String username;

        @NotBlank(message = "email cannot be empty.")
        @Email(message = "email address is not in a valid format.")
        private String email;

        @NotBlank(message = "password cannot be empty.")
        @Length(min = 8, max = 20, message = "password should be between 8-20")
        private String password;

    }
}