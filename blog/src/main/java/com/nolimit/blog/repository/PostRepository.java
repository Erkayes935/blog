package com.nolimit.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nolimit.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}