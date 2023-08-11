package com.example.demo.integration;

import com.example.demo.controller.MemberRestController;
import com.example.demo.controller.dto.request.MemberLoginRequest;
import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberLoginResponse;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("요청객체로 회원가입 후 저장한 내용을 반환한다")
    void registerMember() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("email@")
                .password("password")
                .build();

        given(memberService.register(request)).willReturn(mock(MemberRegisterResponse.class));

        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("요청한 이메일로 이미 가입한 경우 예외가 발생한다")
    void registerMemberWithDuplicatedEmail() throws Exception {
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("email@")
                .password("password")
                .build();

        given(memberService.register(any())).willThrow(ApplicationException.builder()
                .status(HttpStatus.CONFLICT)
                .message("Email is duplicated")
                .build());

         mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isConflict());
    }
    @Test
    @DisplayName("요청객체로 로그인 후 생성한 토큰값을 반환한다")
    void loginMember() throws Exception {
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("email@naver.com")
                .password("encodedPassword")
                .build();

        given(memberService.login(request)).willReturn(mock(MemberLoginResponse.class));

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("요청객체로 로그인 시 회원정보를 찾을 수 없어 예외가 발생한다")
    void loginMemberWithNotFoundEmail() throws Exception {
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("email@naver.com")
                .password("encodedPassword")
                .build();

        given(memberService.login(any())).willThrow(ApplicationException.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Email not founded")
                .build());

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("요청객체로 로그인 시 패스워드가 일치하지 않아 예외가 발생한다")
    void loginMemberWithNotEqualsPassword() throws Exception {
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("email@naver.com")
                .password("encodedPassword")
                .build();

        given(memberService.login(any())).willThrow(ApplicationException.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("Password is invalid")
                .build());

        mockMvc.perform(post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
