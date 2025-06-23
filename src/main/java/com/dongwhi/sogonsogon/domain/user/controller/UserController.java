package com.dongwhi.sogonsogon.domain.user.controller;

import com.dongwhi.sogonsogon.domain.user.dto.request.LoginRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.ReissueRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.SignUpRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.UpdatePasswordRequest;
import com.dongwhi.sogonsogon.domain.user.service.UserService;
import com.dongwhi.sogonsogon.global.common.BaseResponse;
import com.dongwhi.sogonsogon.global.security.jwt.dto.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(@RequestBody SignUpRequest request) {
        userService.signup(request);
        return BaseResponse.of(null, HttpStatus.CREATED.value(), "회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Jwt>> login(@RequestBody LoginRequest request) {
        return BaseResponse.of(userService.login(request), "로그인 성공");
    }

    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<Jwt>> reissue(@RequestBody ReissueRequest request) {
        return BaseResponse.of(userService.reissue(request), "엑세스 토큰 재발급 성공");
    }

    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> updatePassword(@RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return BaseResponse.of(null, "비밀번호 변경 성공");
    }
}