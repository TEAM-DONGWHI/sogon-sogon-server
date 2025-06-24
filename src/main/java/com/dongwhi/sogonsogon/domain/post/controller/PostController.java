package com.dongwhi.sogonsogon.domain.post.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.dongwhi.sogonsogon.domain.post.dto.PostRequestDto;
import com.dongwhi.sogonsogon.global.common.ApiResponse;
import com.dongwhi.sogonsogon.domain.post.dto.PostResponseDto;
import com.dongwhi.sogonsogon.domain.post.entity.Post;
import com.dongwhi.sogonsogon.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal com.dongwhi.sogonsogon.global.security.details.CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        com.dongwhi.sogonsogon.domain.user.entity.User user = userDetails.user();
        postService.createPost(requestDto, user);
        ApiResponse<Object> response = ApiResponse.builder()
                .data(new java.util.HashMap<>())
                .status(0)
                .message("게시물 등록 성공")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
