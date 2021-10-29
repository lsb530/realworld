package com.boki.realworld.api.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("user")
    @Valid
    private NestedClass userInfo;

    @Getter
    @Builder
    public static class NestedClass {

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