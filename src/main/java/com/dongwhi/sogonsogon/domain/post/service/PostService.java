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
}
