package com.example.demo.service;

import com.example.demo.controller.dto.MemberDto;
import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest request){
        //MemberEntity member = MemberEntity.fromDto(memberDto);

        PostEntity newPost = PostEntity.builder()
                        .title(request.getTitle())
                        .body(request.getBody())
                        .build();

        PostEntity savedPost = postRepository.save(newPost);

        return PostCreateResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .build();
    }
}
