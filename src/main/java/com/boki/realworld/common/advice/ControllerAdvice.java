package com.boki.realworld.common.advice;

import com.boki.realworld.common.exception.BadRequestException;
import com.boki.realworld.common.exception.DuplicatedException;
import com.boki.realworld.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
        log.error("BadRequestException : {}", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> badRequestException(DuplicatedException e) {
        log.error("DuplicatedException : {}", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException e) {
        log.error("NotFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        log.error("BindException : {}", e.getMessage());
        return ResponseEntity.unprocessableEntity().body(ErrorResponse.from(e.getBindingResult()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("Exception : {}", e.getMessage());
        return ResponseEntity.internalServerError().body(ErrorResponse.from(e.getMessage()));
    }
}