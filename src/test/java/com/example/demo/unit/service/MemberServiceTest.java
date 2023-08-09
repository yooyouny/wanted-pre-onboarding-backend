package com.example.demo.unit.service;

import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @DisplayName("요청한 내용으로 새로운 멤버를 저장하여 응답객체로 반환한다")
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
}
