package com.dongwhi.sogonsogon.domain.post.controller;

import com.dongwhi.sogonsogon.domain.post.dto.PostRequestDto;
import com.dongwhi.sogonsogon.domain.post.dto.PostResponseDto;
import com.dongwhi.sogonsogon.domain.post.service.PostService;
import com.dongwhi.sogonsogon.global.common.BaseResponse;
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
    public ResponseEntity<BaseResponse<PostResponseDto>> createPost(
            @RequestBody PostRequestDto requestDto) {

        PostResponseDto responseDto = postService.createPost(requestDto);
        return BaseResponse.of(responseDto, HttpStatus.CREATED.value(), "게시물 등록 성공");
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto requestDto) {

        postService.updatePost(postId, requestDto);
        return BaseResponse.of(null, "게시물 수정 성공");
    }
}