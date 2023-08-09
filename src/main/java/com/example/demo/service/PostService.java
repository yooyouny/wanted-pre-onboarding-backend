package com.example.demo.service;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.entity.CustomMemberDetails;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest request, String email){
        MemberEntity writer = memberRepository.findByEmail(email).orElseThrow(() ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Member not founded")
                        .build());

        PostEntity requestPost = PostEntity.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .member(writer)
                .build();

        PostEntity savedPost = postRepository.save(requestPost);

        return PostCreateResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .email(savedPost.getMember().getEmail())
                .build();
    }

    public PostReadResponse getPost(Long postId){
        PostEntity savedPost = postRepository.findById(postId).orElseThrow( () ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(String.format("Post id %s not founded", postId))
                        .build());

        return PostReadResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .build();
    }
}
