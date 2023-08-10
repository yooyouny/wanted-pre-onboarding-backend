package com.example.demo.service;

import com.example.demo.entity.MemberDetails;
import com.example.demo.entity.MemberEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetails loadMemberByEmail(String email) {
        MemberEntity registeredMember = memberRepository.findByEmail(email).orElseThrow(() ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Member not found")
                        .build());

        return MemberDetails.fromEntity(registeredMember);
    }
}
