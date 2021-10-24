package com.example.springbootboard.controller;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.User;
import com.example.springbootboard.dto.*;
import com.example.springbootboard.dto.request.RequestCreatePost;
import com.example.springbootboard.dto.request.RequestPagePost;
import com.example.springbootboard.dto.request.RequestUpdatePost;
import com.example.springbootboard.dto.response.PostDto;
import com.example.springbootboard.repository.PostRepository;
import com.example.springbootboard.repository.UserRepository;
import com.example.springbootboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import java.net.URI;
import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts", produces = MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
@RestController
public class PostApiController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Long> save(@Valid @RequestBody final RequestCreatePost request) {
        Long postId = postService.save(request);
        return ResponseEntity.created(URI.create("/api/v1/posts" + postId)).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDto> getOne(@PathVariable("postId") final Long postId) {
        PostDto post = postService.findOne(postId);

        return ResponseEntity.ok()
                .body(ResponseDto.builder()
                        .data(post)
                        .status(HttpStatus.OK)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAll(@ModelAttribute final RequestPagePost pageable) {
        PagePostDto posts = postService.findAll(pageable.of());

        return ResponseEntity.ok()
                .body(ResponseDto.builder()
                        .data(posts)
                        .status(HttpStatus.OK)
                        .build());
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> update(@PathVariable("postId") final Long postId, @Valid @RequestBody final RequestUpdatePost request) {
        postService.update(postId, request);
        return ResponseEntity.ok()
                .location(URI.create("/api/v1/posts" + postId))
                .build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") final Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent()
                .location(URI.create("/api/v1/posts"))
                .build();
    }
}
