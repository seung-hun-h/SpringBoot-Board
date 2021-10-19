package com.example.springbootboard.controller;

import com.example.springbootboard.domain.Hobby;
import com.example.springbootboard.domain.User;
import com.example.springbootboard.dto.request.RequestCreatePost;
import com.example.springbootboard.dto.request.RequestUpdatePost;
import com.example.springbootboard.dto.UserDto;
import com.example.springbootboard.repository.UserRepository;
import com.example.springbootboard.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class PostApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;


    private ValidatorFactory factory;

    private Validator validator;

    User user = null;

    @BeforeAll
    void setUp() {
        user = User.builder()
                .email("hello123@naver.com")
                .password("password!ASV123@")
                .createdAt(LocalDateTime.now())
                .createdBy("seunghun")
                .name("seunghun")
                .build();

        user.login(user.getPassword());

        userRepository.save(user);
    }

    @AfterEach
    void restore() {
//        requestUser = UserDto.builder()
//                .age(27)
//                .name("seunghun")
//                .hobby(Hobby.SPORTS)
//                .build();
    }

    @Test
    @DisplayName("게시물을 저장한다")
    public void testSavePost() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();


        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions actions = mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        actions
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post-save",
                        requestFields(
                            fieldWithPath("title").type(STRING).description("title"),
                            fieldWithPath("content").type(STRING).description("content"),
                            fieldWithPath("userId").type(NUMBER).description("userId")
                        )
                ));
    }


    @Test
    @DisplayName("게시물을 단건 조회한다")
    public void testGetOne() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();

        Long postId = postService.save(request);

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/posts/{postId}", postId)
                .characterEncoding("utf8")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.title").value("나는 전설이다"))
                .andExpect(jsonPath("data.content").value("좀비 세상에서 살아남기"))
                .andDo(print())
                .andDo(document("post-get-one",
                        pathParameters(
                            parameterWithName("postId").description("postId")
                        ),
                        responseFields(
                            fieldWithPath("status").type(STRING).description("status"),
                            fieldWithPath("data").type(OBJECT).description("data"),
                            fieldWithPath("data.postId").type(NUMBER).description("postId"),
                            fieldWithPath("data.title").type(STRING).description("title"),
                            fieldWithPath("data.content").type(STRING).description("content"),
                            fieldWithPath("data.createdAt").type(STRING).description("createdAt"),
                            fieldWithPath("data.createdBy").type(STRING).description("createdBy"),
                            fieldWithPath("serverDateTime").type(STRING).description("serverDateTime")

                        )
                ));

    }

    @Test
    @DisplayName("게시물을 다건 조회한다")
    public void testGetAll() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();

        Long postId = postService.save(request);

        //when
        ResultActions actions = mockMvc.perform(get("/api/v1/posts")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .characterEncoding("utf8")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get-all",
                        responseFields(
                            fieldWithPath("status").type(STRING).description("status"),
                            fieldWithPath("data").type(OBJECT).description("data"),
                            fieldWithPath("data.page").type(NUMBER).description("page"),
                            fieldWithPath("data.size").type(NUMBER).description("size"),
                            fieldWithPath("data.first").type(BOOLEAN).description("first"),
                            fieldWithPath("data.last").type(BOOLEAN).description("last"),
                            fieldWithPath("data.totalPages").type(NUMBER).description("totalPages"),
                            fieldWithPath("data.totalElements").type(NUMBER).description("totalElements"),
                            fieldWithPath("data.posts[]").type(ARRAY).description("posts"),
                            fieldWithPath("data.posts[].postId").type(NUMBER).description("postId"),
                            fieldWithPath("data.posts[].title").type(STRING).description("title"),
                            fieldWithPath("data.posts[].content").type(STRING).description("content"),
                            fieldWithPath("data.posts[].createdAt").type(STRING).description("createdAt"),
                            fieldWithPath("data.posts[].createdBy").type(STRING).description("createdBy"),
                            fieldWithPath("serverDateTime").type(STRING).description("serverDateTime")
                        )
                ));

    }

    @Test
    @DisplayName("게시물을 수정할 수 있다")
    public void testUpdatePost() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();

        Long postId = postService.save(request);

        RequestUpdatePost update = RequestUpdatePost.builder()
                .content("update content")
                .title("update title")
                .userId(user.getId())
                .build();

        String json = objectMapper.writeValueAsString(update);

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json));

        //then
        actions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-update",
                        pathParameters(
                                parameterWithName("postId").description("postId")
                        ),
                        requestFields(
                                fieldWithPath("userId").type(NUMBER).description("userId"),
                                fieldWithPath("title").type(STRING).description("title"),
                                fieldWithPath("content").type(STRING).description("content")
                        ))
                );
    }

    @Test
    @DisplayName("게시물을 삭제할 수 있다")
    public void testDeletePost() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();

        Long postId = postService.save(request);

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/posts/{postId}", postId));

        //then
        actions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("postId")
                        ))
                );
    }

    @Test
    @DisplayName("게시물 부재 예외 테스트")
    public void testIllegalArgumentException() throws Exception {
        //given
        RequestCreatePost request = RequestCreatePost.builder()
                .userId(user.getId())
                .title("나는 전설이다")
                .content("좀비 세상에서 살아남기")
                .build();

        Long postId = postService.save(request);

        RequestUpdatePost update = RequestUpdatePost.builder()
                .content("update content")
                .title("update title")
                .userId(user.getId())
                .build();

        String json = objectMapper.writeValueAsString(update);

        //when
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/posts/{postId}", postId + 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json));

        //then
        actions.andExpect(status().isInternalServerError())
                .andDo(print());
    }
}
