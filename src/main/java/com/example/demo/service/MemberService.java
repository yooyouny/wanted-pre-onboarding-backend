package com.example.demo.service;

import com.example.demo.controller.dto.request.MemberLoginRequest;
import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberLoginResponse;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.utill.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Value("${jwt.secret-key}")
    private String secretKey;


    @Transactional
    public MemberRegisterResponse register(MemberRegisterRequest request){
        memberRepository.findByEmail(request.getEmail()).ifPresent(member -> {
                throw ApplicationException.builder()
                        .status(HttpStatus.CONFLICT)
                        .message("Email is duplicated")
                        .build();
        });

        MemberEntity memberEntity = MemberEntity.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();

       MemberEntity registeredMember = memberRepository.save(memberEntity);

       return MemberRegisterResponse.fromEntity(registeredMember);
    }
    public MemberLoginResponse login(MemberLoginRequest request){

        MemberEntity registeredMember = memberRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(String.format("Email %s not founded", request.getEmail()))
                        .build()
        );

        if(!encoder.matches(request.getPassword(), registeredMember.getPassword())){
            throw ApplicationException.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Password is invalid")
                    .build();
        }

        String token = JwtTokenUtils.generateToken(request.getEmail(), secretKey, expiredTimeMs);

        return MemberLoginResponse.builder()
                .token(token)
                .build();
    }
}
