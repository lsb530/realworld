package com.boki.realworld.api.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty("user")
    @Valid
    private NestedClass userInfo;

    @Getter
    @Builder
    public static class NestedClass {

        @NotBlank(message = "email cannot be empty.")
        @Email(message = "email address is not in a valid format.")
        private String email;

        @NotBlank(message = "password cannot be empty.")
        @Length(min = 8, max = 20, message = "password should be between 8-20")
        private String password;
    }

}