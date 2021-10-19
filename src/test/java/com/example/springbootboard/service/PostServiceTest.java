package com.example.springbootboard.service;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.Post;
import com.example.springbootboard.domain.Title;
import com.example.springbootboard.domain.User;
import com.example.springbootboard.dto.*;
import com.example.springbootboard.dto.request.RequestCreatePost;
import com.example.springbootboard.dto.request.RequestUpdatePost;
import com.example.springbootboard.dto.response.PostDto;
import com.example.springbootboard.repository.PostRepository;
import com.example.springbootboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    User user = null;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .email("hello123@naver.com")
                .password("passwordABC!123@")
                .name("seunghun")
                .createdAt(LocalDateTime.now())
                .createdBy("seunghun")
                .build());
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void testAutowired() throws Exception {
        assertThat(postService).isNotNull();
        assertThat(userRepository).isNotNull();
    }
    
    @Test
    @DisplayName("게시글이 저장된다")
    public void testPostSave() throws Exception {
        //given
        user.login(user.getPassword());

        RequestCreatePost request = RequestCreatePost.builder()
                .content("content")
                .title("title")
                .userId(user.getId())
                .build();

        //when
        Long postId = postService.save(request);

        //then
        Optional<Post> actual = postRepository.findById(postId);
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(postId);
        assertThat(actual.get().getContent()).isEqualTo(request.getContent());
        assertThat(actual.get().getTitle()).isEqualTo(new Title(request.getTitle()));
    }

    @Test 
    @DisplayName("게시글을 단건 조회한다")
    public void testFindOne() throws Exception {
        //given
        user.login(user.getPassword());
        RequestCreatePost request = RequestCreatePost.builder()
                .content("content")
                .title("title")
                .userId(user.getId())
                .build();

        Long postId = postService.save(request);

        //when
        PostDto response = postService.findOne(postId);

        //then
        assertThat(response.getPostId()).isEqualTo(postId);
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
    } 
    
    @Test 
    @DisplayName("게시글을 모두 조회한다")
    public void testFindAll() throws Exception {
        //given
        user.login(user.getPassword());
        RequestCreatePost request1 = RequestCreatePost.builder()
                .content("content1")
                .title("title1")
                .userId(user.getId())
                .build();

        RequestCreatePost request2 = RequestCreatePost.builder()
                .content("content2")
                .title("title2")
                .userId(user.getId())
                .build();

        RequestCreatePost request3 = RequestCreatePost.builder()
                .content("content3")
                .title("title3")
                .userId(user.getId())
                .build();

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        postService.save(request1);
        postService.save(request2);
        postService.save(request3);

        //then
        PagePostDto actual = postService.findAll(pageRequest);
        assertThat(actual.getPosts()).hasSize(3);
        log.info("page: {}", actual);
    }

    @Test
    @DisplayName("게시글을 수정한다")
    public void testUpdatePost() throws Exception {
        //given
        user.login(user.getPassword());

        RequestCreatePost requestCreate = RequestCreatePost.builder()
                .content("content")
                .title("title")
                .userId(user.getId())
                .build();

        Long postId = postService.save(requestCreate);

        RequestUpdatePost requestUpdate = RequestUpdatePost.builder()
                .title("update title")
                .content("update content")
                .userId(user.getId())
                .build();

        //when
        Long id = postService.update(postId, requestUpdate);

        //then
        Optional<Post> actual = postRepository.findById(postId);
        assertThat(actual).isPresent();
        assertThat(actual.get().getContent()).isEqualTo("update content");
        assertThat(actual.get().getTitle()).isEqualTo(new Title("update title"));
    }
}
