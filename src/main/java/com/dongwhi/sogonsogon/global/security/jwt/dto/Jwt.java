package com.dongwhi.sogonsogon.global.security.jwt.dto;

public record Jwt(
        String accessToken,
        String refreshToken
) {
}