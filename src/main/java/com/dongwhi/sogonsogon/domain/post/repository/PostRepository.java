package com.dongwhi.sogonsogon.domain.post.repository;

import com.dongwhi.sogonsogon.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
