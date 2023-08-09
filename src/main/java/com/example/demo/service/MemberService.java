package com.example.demo.service;

import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
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

       return MemberRegisterResponse.builder()
               .id(registeredMember.getId())
               .email(registeredMember.getEmail())
               .password(registeredMember.getPassword())
               .registedAt(registeredMember.getCreatedDateTIme())
               .build();
    }
}
