package com.nolimit.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nolimit.blog.dto.PostRequest;
import com.nolimit.blog.dto.PostResponse;
import com.nolimit.blog.entity.Post;
import com.nolimit.blog.entity.User;
import com.nolimit.blog.repository.PostRepository;
import com.nolimit.blog.repository.UserRepository;

@Service
public class PostService {

        private final PostRepository postRepository;
        private final UserRepository userRepository;

        public PostService(PostRepository postRepository,
                        UserRepository userRepository) {
                this.postRepository = postRepository;
                this.userRepository = userRepository;
        }

        public PostResponse create(PostRequest request) {

                String email = getCurrentUserEmail();

                User author = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

                Post post = Post.builder()
                                .content(request.getContent())
                                .author(author)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                postRepository.save(post);

                return PostResponse.builder()
                                .id(post.getId())
                                .content(post.getContent())
                                .author(author.getName())
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .build();
        }

        public List<PostResponse> findAll() {

                return postRepository.findAll()
                                .stream()
                                .map(post -> PostResponse.builder()
                                                .id(post.getId())
                                                .content(post.getContent())
                                                .author(post.getAuthor().getName())
                                                .createdAt(post.getCreatedAt())
                                                .updatedAt(post.getUpdatedAt())
                                                .build())
                                .toList();
        }

        public PostResponse findById(Long id) {

                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));

                return PostResponse.builder()
                                .id(post.getId())
                                .content(post.getContent())
                                .author(post.getAuthor().getName())
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .build();
        }

        public PostResponse update(Long id, PostRequest request) {

                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));

                String email = getCurrentUserEmail();

                if (!post.getAuthor().getEmail().equals(email)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Anda bukan pemilik post");
                }
                
                post.setContent(request.getContent());
                post.setUpdatedAt(LocalDateTime.now());

                postRepository.save(post);

                return PostResponse.builder()
                                .id(post.getId())
                                .content(post.getContent())
                                .author(post.getAuthor().getName())
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .build();
        }

        public String delete(Long id) {

                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Post tidak ditemukan"));

                String email = getCurrentUserEmail();

                if (!post.getAuthor().getEmail().equals(email)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "Anda bukan pemilik post");
                }
                
                postRepository.delete(post);

                return "Delete success";
        }

        private String getCurrentUserEmail() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                return authentication.getName();
        }
}