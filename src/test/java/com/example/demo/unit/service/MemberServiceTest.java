package com.example.demo.unit.service;

import com.example.demo.controller.dto.request.MemberLoginRequest;
import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("요청한 내용으로 회원가입을 진행하여 저장한 내용을 응답객체로 반환한다")
    void registerMember(){
        //given
        String email = "email@";
        String encodedPassword = "encodedPassword";
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email(email)
                .password(encodedPassword)
                .build();
        MemberEntity savedMember = MemberEntity.of(request);
        savedMember.setIdForTest(1L);

        given(memberRepository.save(any())).willReturn(savedMember);
        given(encoder.encode(anyString())).willReturn(encodedPassword);

        //when
        MemberRegisterResponse response = memberService.register(request);

        //then
        Assertions.assertEquals(response.getId(), 1L);
        Assertions.assertEquals(response.getEmail(), email);
        Assertions.assertEquals(response.getPassword(), request.getPassword());
    }
    @Test
    @DisplayName("요청한 아이디로는 이미 가입되어있어 예외가 발생한다")
    void registerMemberWithDuplicatedId(){
        //given
        String email = "email@";
        String password = "password";
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email(email)
                .password(password)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(MemberEntity.of(request)));

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, ()
                -> memberService.register(request));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.CONFLICT);
    }

    // 로그인 성공 테스트는 통합테스트에서 진행
    @Test
    @DisplayName("요청한 아이디가 가입되어있지 않아 로그인이 실패한다")
    void loginMemberWithNotFoundId(){
        //given
        String email = "email@";
        String encodedPassword = "encodedPassword";
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, ()
                -> memberService.login(request));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("요청한 비밀번호가 일치하지않아 로그인이 실패한다")
    void loginMemberWithWrongPassword(){
        //given
        String email = "email@";
        String encodedPassword = "encodedPassword";
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(MemberEntity.of(request)));
        given(encoder.matches(anyString(), anyString())).willReturn(false);

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, ()
                -> memberService.login(request));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.UNAUTHORIZED);
    }
}
