package com.dongwhi.sogonsogon.domain.user.dto.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {

    private String currentPassword;

    private String newPassword;
}