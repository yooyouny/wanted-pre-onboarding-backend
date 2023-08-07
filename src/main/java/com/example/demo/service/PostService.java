package com.example.demo.service;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.entity.PostEntity;
import com.example.demo.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest request){
        //MemberEntity member = MemberEntity.fromDto(memberDto);

        PostEntity savedPost = postRepository.save(PostEntity.of(request));

        return PostCreateResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .build();
    }
}
