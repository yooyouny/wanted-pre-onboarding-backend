package com.example.demo.unit.controller;

import com.example.demo.controller.PostRestController;
import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @InjectMocks
    private PostRestController postRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(postRestController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    @DisplayName("요청객체로 포스트 생성 요청 시 필수값 검증에서 예외가 발생한다")
    void createPostWithBlankRequest() throws Exception {
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("")
                .body("")
                .build();

        mockMvc.perform(post("/api/v1/posts")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
    @Test
    @DisplayName("요청객체로 포스트 수정 요청 시 필수값 검증에서 예외가 발생한다")
    void ModifyPostWithBlankRequest() throws Exception {
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("")
                .body("body")
                .build();

        mockMvc.perform(put("/api/v1/posts")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
