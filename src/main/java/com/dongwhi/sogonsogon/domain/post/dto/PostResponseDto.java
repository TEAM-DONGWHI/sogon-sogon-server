package com.dongwhi.sogonsogon.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private Long userId;
}