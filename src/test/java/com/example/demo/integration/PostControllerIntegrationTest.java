package com.example.demo.integration;

import com.example.demo.controller.dto.request.PostCreateRequest;

import com.example.demo.controller.dto.request.PostModifyRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.entity.MemberDetails;
import com.example.demo.exception.ApplicationException;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;
    
    private void setMemberDetailsForTest(){
        MemberDetails userDetails = new MemberDetails(
                1L, "email@", "password", LocalDateTime.now(), LocalDateTime.now());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @WithUserDetails()
    @DisplayName("요청객체로 새로운 포스트를 저장하고 응답객체로 반환한다")
    void createPost() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("test1")
                .body("contents")
                .build();
        PostCreateResponse response = PostCreateResponse.builder()
                .title("test1")
                .body("contents")
                .email("email@")
                .build();
        setMemberDetailsForTest();
        given(postService.create(request, "email@")).willReturn(response);

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("포스트 등록 시 로그인을 하지 않은 경우 예외가 발생한다")
    void createPostWithNotLoginMember() throws Exception {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("test1")
                .body("contents")
                .build();

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("포스트 저장을 요청하는 이메일이 존재하지 않아 예외가 발생한다")
    void createPostWithNotFoundEmail() throws Exception {
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("title")
                .body("body")
                .build();
        setMemberDetailsForTest();
        willThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Member not founded")
                .build())
                .given(postService).create(any(), anyString());
        //when
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("요청한 포스트 아이디로 포스트를 찾아 응답 객체로 반환한다")
    void getPostByPostId() throws Exception {
        Long requestedpostId = 1L;
        PostReadResponse response = PostReadResponse.builder()
                .id(requestedpostId)
                .title("title")
                .body("body")
                .email("email@")
                .build();

        when(postService.getPost(requestedpostId)).thenReturn(response);
        mockMvc.perform(get("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id", is(1)),
                        jsonPath("$.data.title", is("title")),
                        jsonPath("$.data.body", is("body"))
                );
    }

    @Test
    @WithMockUser
    @DisplayName("요청한 포스트가 존재하지 않아 예외가 발생한다")
    void getPostWithNotFoundPost() throws Exception {
        //given
        Long requestedPostId = 1L;
        willThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Post not founded")
                .build())
                .given(postService).getPost(requestedPostId);
        //when
        mockMvc.perform(get("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("저장된 포스트들을 Page 객체에 담아 반환한다")
    void getAllPost() throws Exception {
        //given
        PostReadResponse post1 = PostReadResponse.builder()
                .id(1L)
                .title("title1")
                .body("body1")
                .email("email1")
                .build();
        PostReadResponse post2 = PostReadResponse.builder()
                .id(2L)
                .title("title2")
                .body("body2")
                .email("email2")
                .build();
        List<PostReadResponse> mockPostList = List.of(post1, post2);
        Page<PostReadResponse> mockPostPage = new PageImpl<>(mockPostList);
        given(postService.getAllPost(anyInt(), anyInt())).willReturn(mockPostPage);
        // when
        mockMvc.perform(get("/api/v1/posts")
                .param("pageNo", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.content[0].id").value(1),
                        jsonPath("$.data.content[0].title").value("title1"),
                        jsonPath("$.data.content[0].body").value("body1"),
                        jsonPath("$.data.content[0].email").value("email1"),
                        jsonPath("$.data.content[1].id").value(2),
                        jsonPath("$.data.content[1].title").value("title2"),
                        jsonPath("$.data.content[1].body").value("body2"),
                        jsonPath("$.data.content[1].email").value("email2")
                );
    }

    @Test
    @WithMockUser
    @DisplayName("요청한 포스트 아이디로 등록된 포스트를 찾아 요청한 내용으로 수정하여 반환한다")
    void modifyPostByPostId() throws Exception {
        //given
        Long requestedPostId = 1L;
        PostModifyRequest request = PostModifyRequest.builder()
                .title("modified")
                .body("modified")
                .build();
        PostReadResponse response = PostReadResponse.builder()
                .id(requestedPostId)
                .title("modified")
                .body("modified")
                .email("email@")
                .build();
        setMemberDetailsForTest();
        given(postService.modify(any(), any(), anyString())).willReturn(response);
        //when
        mockMvc.perform(put("/api/v1/posts/1")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        //then
                        status().isOk(),
                        jsonPath("$.data.id", is(1)),
                        jsonPath("$.data.title", is("modified")),
                        jsonPath("$.data.body", is("modified"))
                );
    }

    @Test
    @WithAnonymousUser
    @DisplayName("포스트 수정 시 로그인을 하지 않은 경우 예외가 발생한다")
    void ModifyPostWithNotLoginMember() throws Exception {
        //given
        PostModifyRequest request = PostModifyRequest.builder()
                .title("test1")
                .body("contents")
                .build();
        //when
        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                //then
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("수정 할 포스트 아이디가 존재하지 않아 예외가 발생한다")
    void ModifyPostWithNotFoundPost() throws Exception {
        //given
        PostModifyRequest request = PostModifyRequest.builder()
                .title("title")
                .body("body")
                .build();
        setMemberDetailsForTest();
        willThrow(ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Post not founded")
                        .build()
        ).given(postService).modify(any(), any(), anyString());

        //when
        mockMvc.perform(put("/api/v1/posts/1")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser
    @DisplayName("포스트 수정을 요청한 이메일이 존재하지 않아 예외가 발생한다")
    void modifyPostWithNotFoundEmail() throws Exception {
        //given
        PostModifyRequest request = PostModifyRequest.builder()
                .title("title")
                .body("body")
                .build();
        setMemberDetailsForTest();
        given(postService.modify(any(), any(), anyString())).willThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Member not founded")
                .build()
        );
        //when
        mockMvc.perform(put("/api/v1/posts/1")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("수정을 요청한 회원과 포스트를 등록한 회원이 일치하지 않아 예외가 발생한다")
    void ModifyPostByNotWriter() throws Exception {
        //given
        setMemberDetailsForTest();
        willThrow(ApplicationException.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("Only the writer of the post is modify it")
                .build()
        ).given(postService).delete(any(), anyString());
        //when
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("요청한 포스트 아이디로 포스트를 찾아 삭제한다")
    void deletePost() throws Exception {
        //given
        Long requestedPostId = 1L;
        String email = "email@";
        setMemberDetailsForTest();

        //when
        doNothing().when(postService).delete(requestedPostId, email);

        //then
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser
    @DisplayName("포스트 삭제 시 로그인을 하지 않은 경우 예외가 발생한다")
    void DeletePostWithNotLoginMember() throws Exception {
        Long requestedId = 1L;

        mockMvc.perform(delete("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestedId))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    @DisplayName("삭제할 포스트가 존재하지 않아 예외가 발생한다")
    void deletePostWithNotFoundPostId() throws Exception {
        Long requestedPostId = 1L;
        String email = "email@";
        setMemberDetailsForTest();

        willThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Post not founded")
                .build())
                .given(postService).delete(requestedPostId, email);

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("삭제를 요청한 이메일이 존재하지 않아 예외가 발생한다")
    void deletePostWithNotFoundEmail() throws Exception {
        //given
        setMemberDetailsForTest();
        doThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Email not founded")
                .build())
                .when(postService).delete(any(), anyString());
        //when
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("삭제를 요청한 회원과 포스트를 등록한 회원이 일치하지 않아 예외가 발생한다")
    void deletePostByNotWriter() throws Exception {
        //given
        setMemberDetailsForTest();
        willThrow(ApplicationException.builder()
                .status(HttpStatus.FORBIDDEN)
                .message("Only the writer of the post is modify it")
                .build()
        ).given(postService).delete(any(), anyString());
        //when
        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //then
                .andExpect(status().isForbidden());
    }
}
