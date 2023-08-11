package com.example.demo.unit.controller;

import com.example.demo.controller.MemberRestController;
import com.example.demo.controller.dto.request.MemberRegisterRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    @InjectMocks
    private MemberRestController memberRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberRestController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("요청객체로 멤버 회원가입 요청 시 필수값 검증에서 예외가 발생한다")
    void createMemberWithBlankRequest() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(post("/api/v1/members/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("요청객체로 멤버 회원가입 요청 시 비밀번호가 8자 이하라 검증에서 예외가 발생한다")
    void createMemberWithPasswordLessThan8() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("email@")
                .password("pass")
                .build();

        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("요청객체로 멤버 회원가입 요청 시 이메일 문자 검증에서 예외가 발생한다")
    void createMemberWithInvalidEmail() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("email")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("요청객체로 로그인 요청 시 필수값 검증에서 예외가 발생한다")
    void createLoginWithBlankRequest() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @Test
    @DisplayName("요청객체로 로그인 요청 시 이메일 문자 검증에서 예외가 발생한다")
    void createLoginWithInvalidEmail() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("email")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
