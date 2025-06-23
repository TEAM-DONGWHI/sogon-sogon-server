package com.dongwhi.sogonsogon.global.security.details;

import com.dongwhi.sogonsogon.domain.user.entity.User;
import com.dongwhi.sogonsogon.domain.user.repository.UserRepository;
import com.dongwhi.sogonsogon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("찾을 수 없는 유저", HttpStatus.NOT_FOUND));

        List<GrantedAuthority> authorities;

        if (user.getRole() == null) {
            authorities = Collections.emptyList();
        } else {
            authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}