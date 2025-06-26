package com.dongwhi.sogonsogon.domain.user.service;

import com.dongwhi.sogonsogon.domain.user.dto.request.LoginRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.ReissueRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.SignUpRequest;
import com.dongwhi.sogonsogon.domain.user.dto.request.UpdatePasswordRequest;
import com.dongwhi.sogonsogon.domain.user.entity.User;
import com.dongwhi.sogonsogon.domain.user.enums.UserRole;
import com.dongwhi.sogonsogon.domain.user.repository.UserRepository;
import com.dongwhi.sogonsogon.global.exception.CustomException;
import com.dongwhi.sogonsogon.global.security.jwt.dto.Jwt;
import com.dongwhi.sogonsogon.global.security.jwt.enums.JwtType;
import com.dongwhi.sogonsogon.global.security.jwt.provider.JwtProvider;
import com.dongwhi.sogonsogon.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final SecurityUtil securityUtil;

    public void signup(SignUpRequest request) {
         if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException("이미 존재하는 유저 이름",  HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    public Jwt login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("찾을 수 없는 유저", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("잘못된 비밀번호", HttpStatus.BAD_REQUEST);
        }

        return jwtProvider.generateToken(request.getUsername());
    }

    public Jwt reissue(ReissueRequest request) {
        String email = jwtProvider.getSubject(request.getRefreshToken());

        if (userRepository.findByUsername(email).isEmpty()) {
            throw new CustomException("찾을 수 없는 유저", HttpStatus.NOT_FOUND);
        }

        if (jwtProvider.getType(request.getRefreshToken()) != JwtType.REFRESH)
            throw new CustomException("유효하지 않은 토큰 타입", HttpStatus.BAD_REQUEST);

        return jwtProvider.generateToken(email);
    }

    public void updatePassword(UpdatePasswordRequest request) {
        User user = securityUtil.currentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException("잘못된 비밀번호", HttpStatus.BAD_REQUEST);
        }

        User updatedUser = user.toBuilder()
                .password(passwordEncoder.encode(request.getNewPassword()))
                .build();

        userRepository.save(updatedUser);
    }
}