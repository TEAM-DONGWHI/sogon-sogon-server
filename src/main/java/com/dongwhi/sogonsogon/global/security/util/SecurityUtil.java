package com.dongwhi.sogonsogon.global.security.util;

import com.dongwhi.sogonsogon.domain.user.entity.User;
import com.dongwhi.sogonsogon.domain.user.repository.UserRepository;
import com.dongwhi.sogonsogon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;
    /**
     * 현재 인증된 사용자의 정보를 반환합니다.
     * @return 현재 인증된 사용자의 User 엔티티
     * @throws CustomException 인증되지 않은 사용자일 경우 발생
     * @throws CustomException 유저를 찾을 수 없을 경우 발생
     */
    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new CustomException("인증되지 않은 유저", HttpStatus.UNAUTHORIZED);
        }
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException("찾을 수 없는 유저", HttpStatus.NOT_FOUND));
    }
}