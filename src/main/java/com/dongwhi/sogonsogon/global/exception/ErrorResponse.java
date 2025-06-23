package com.dongwhi.sogonsogon.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public record ErrorResponse(
        HttpStatus status,
        String message,
        LocalDateTime localDateTime
) {
    public static ResponseEntity<ErrorResponse> of(CustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(
                        e.getStatus(),
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }
}