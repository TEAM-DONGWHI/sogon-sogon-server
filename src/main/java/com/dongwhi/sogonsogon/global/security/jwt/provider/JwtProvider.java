package com.dongwhi.sogonsogon.global.security.jwt.provider;

import com.dongwhi.sogonsogon.domain.user.entity.User;
import com.dongwhi.sogonsogon.domain.user.repository.UserRepository;
import com.dongwhi.sogonsogon.global.exception.CustomException;
import com.dongwhi.sogonsogon.global.security.details.CustomUserDetails;
import com.dongwhi.sogonsogon.global.security.jwt.config.JwtProperties;
import com.dongwhi.sogonsogon.global.security.jwt.dto.Jwt;
import com.dongwhi.sogonsogon.global.security.jwt.enums.JwtType;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private SecretKey key;

    @PostConstruct
    protected void init() {
        key = new SecretKeySpec(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS512.key().build().getAlgorithm()
        );
    }

    public Jwt generateToken(String email) {
        Date now = new Date();
        String accessToken = createToken(email, JwtType.ACCESS, now,
                Duration.ofHours(jwtProperties.getAccessTokenExpiration()));
        String refreshToken = createToken(email, JwtType.REFRESH, now,
                Duration.ofDays(jwtProperties.getRefreshTokenExpiration()));

        return new Jwt(accessToken, refreshToken);
    }

    private String createToken(String username, JwtType type, Date issuedAt, Duration duration) {
        return Jwts.builder()
                .header()
                .type(type.name())
                .and()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(new Date(issuedAt.getTime() + duration.toMillis()))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        if (getType(token) != JwtType.ACCESS) {
            throw new CustomException("유효하지 않은 토큰",  HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() -> new CustomException("사용자를 찾지 못함", HttpStatus.NOT_FOUND));

        UserDetails details = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException("", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            throw new CustomException("    ", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException e) {
            throw new CustomException("  ", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new CustomException(" ", HttpStatus.BAD_REQUEST);
        }
    }

    public JwtType getType(String token) {
        return JwtType.valueOf(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getHeader().getType()
        );
    }
}