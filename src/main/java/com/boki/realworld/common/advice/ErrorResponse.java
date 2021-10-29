package com.boki.realworld.common.advice;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private ErrorData errors;

    public static ErrorResponse from(BindingResult bindingResult) {
        return new ErrorResponse(ErrorData.from(bindingResult));
    }

    public static ErrorResponse from(String message) {
        return new ErrorResponse(ErrorData.from(message));
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ErrorData {

        private List<String> body;

        public static ErrorData from(BindingResult bindingResult) {
            List<String> collect = bindingResult.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
            return new ErrorData(collect);
        }

        public static ErrorData from(String message) {
            return new ErrorData(List.of(message));
        }

    }
}