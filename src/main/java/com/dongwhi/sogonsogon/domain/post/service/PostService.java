package com.dongwhi.sogonsogon.domain.post.service;

import com.dongwhi.sogonsogon.domain.post.dto.PostRequestDto;
import com.dongwhi.sogonsogon.domain.post.entity.Post;
import com.dongwhi.sogonsogon.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post createPost(PostRequestDto requestDto, com.dongwhi.sogonsogon.domain.user.entity.User user) {
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();
        return postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto, com.dongwhi.sogonsogon.domain.user.entity.User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인 게시글만 수정할 수 있습니다.");
        }
        post = Post.builder()
            .id(post.getId())
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .user(user)
            .build();
        postRepository.save(post);
    }
}
