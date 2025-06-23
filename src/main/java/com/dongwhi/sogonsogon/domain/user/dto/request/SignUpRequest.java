package com.dongwhi.sogonsogon.domain.user.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String username;

    private String password;

    private String name;
}