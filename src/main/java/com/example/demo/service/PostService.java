package com.example.demo.service;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.request.PostModifyRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PostCreateResponse create(PostCreateRequest request, String email){
        MemberEntity writer = getMemberByEmail(email);

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
                .registerAt(savedPost.getCreatedDateTIme())
                .build();
    }
    public PostReadResponse getPost(Long postId){
        PostEntity savedPost = getExistingPostById(postId);

        return PostReadResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .registedAt(savedPost.getCreatedDateTIme())
                .modifiedAt(savedPost.getModifiedDateTime())
                .build();
    }
    @Transactional
    public PostReadResponse modify(Long postId, PostModifyRequest request, String email){
        PostEntity post = getExistingPostById(postId);
        MemberEntity writer = getMemberByEmail(email);

        checkOwnership(post, writer);

        post.modifyPost(request);
        PostEntity modifiedPost = postRepository.saveAndFlush(post);

        return PostReadResponse.builder()
                .id(modifiedPost.getId())
                .title(modifiedPost.getTitle())
                .body(modifiedPost.getBody())
                .email(modifiedPost.getMember().getEmail())
                .registedAt(modifiedPost.getCreatedDateTIme())
                .modifiedAt(modifiedPost.getModifiedDateTime())
                .build();
    }
    @Transactional
    public void delete(Long postId, String email){
        PostEntity post = getExistingPostById(postId);
        MemberEntity writer = getMemberByEmail(email);

        checkOwnership(post, writer);

        post.deletePost();
    }
    private PostEntity getExistingPostById(Long postId){
        return postRepository.findById(postId).orElseThrow( () ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Post not founded")
                        .build());
    }
    private MemberEntity getMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(() ->
                ApplicationException.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Member not founded")
                        .build());
    }
    private void checkOwnership(PostEntity post, MemberEntity writer){
        if(writer.getId() != post.getMember().getId()){
            throw ApplicationException.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Only the Member of the post is modify it")
                    .build();
        }
    }
}
