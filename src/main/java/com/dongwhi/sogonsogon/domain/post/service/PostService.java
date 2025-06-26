package com.dongwhi.sogonsogon.domain.post.service;

import com.dongwhi.sogonsogon.domain.post.dto.PostRequestDto;
import com.dongwhi.sogonsogon.domain.post.dto.PostResponseDto;
import com.dongwhi.sogonsogon.domain.post.dto.PostListResponseDto;
import com.dongwhi.sogonsogon.domain.post.entity.Post;
import com.dongwhi.sogonsogon.domain.post.repository.PostRepository;
import com.dongwhi.sogonsogon.domain.user.entity.User;
import com.dongwhi.sogonsogon.global.exception.CustomException;
import com.dongwhi.sogonsogon.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {
        User user = securityUtil.currentUser();

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponseDto.builder()
                .postId(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .build();
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto requestDto) {
        User user = securityUtil.currentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException("본인 게시글만 수정할 수 있습니다", HttpStatus.FORBIDDEN);
        }

        Post updatedPost = post.toBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        postRepository.save(updatedPost);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> getPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> PostListResponseDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId) {
        User user = securityUtil.currentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException("본인 게시글만 삭제할 수 있습니다", HttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
    }
}