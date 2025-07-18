package com.dongwhi.sogonsogon.domain.post.controller;

import com.dongwhi.sogonsogon.domain.post.dto.PostRequestDto;
import com.dongwhi.sogonsogon.domain.post.dto.PostResponseDto;
import com.dongwhi.sogonsogon.domain.post.dto.PostListResponseDto;
import com.dongwhi.sogonsogon.domain.post.service.PostService;
import com.dongwhi.sogonsogon.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import java.util.List;
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

    @GetMapping
    public ResponseEntity<BaseResponse<List<PostListResponseDto>>> getPosts() {
        List<PostListResponseDto> posts = postService.getPosts();
        return BaseResponse.of(posts, "게시물 목록 조회 성공");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
            @PathVariable Long postId) {

        postService.deletePost(postId);
        return BaseResponse.of(null, "게시물 삭제 성공");
    }
}