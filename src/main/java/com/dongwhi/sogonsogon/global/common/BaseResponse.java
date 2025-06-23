package com.dongwhi.sogonsogon.global.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record BaseResponse<T>(
        T data,
        int status,
        String message
) {

    public static <T> ResponseEntity<BaseResponse<T>> of(T data, String message) {
        return BaseResponse.of(data, HttpStatus.OK.value(), message);
    }

    public static <T> ResponseEntity<BaseResponse<T>> of(T data, int status, String message) {
        return ResponseEntity.status(status).body(new BaseResponse<>(data, status, message));
    }
}