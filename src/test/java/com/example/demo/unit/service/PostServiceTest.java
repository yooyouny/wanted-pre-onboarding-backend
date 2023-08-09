package com.example.demo.unit.service;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.entity.CustomMemberDetails;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("요청한 내용으로 새로운 포스트를 저장하여 응답객체로 반환한다")
    void savedPostTest(){
        //given
        PostCreateRequest requestPost = PostCreateRequest.builder()
                .title("test")
                .body("contents")
                .build();

        MemberEntity member = MemberEntity.builder()
                .email("email@")
                .password("password")
                .build();

        PostEntity savedPost = PostEntity.of(requestPost, member);
        savedPost.setIdForTest(1L);
        given(postRepository.save(any())).willReturn(savedPost);

        //when
        PostCreateResponse response = postService.create(requestPost, member.getEmail());

        //then
        Assertions.assertEquals(response.getId(), 1L);
        Assertions.assertEquals(response.getTitle(), "test");
        Assertions.assertEquals(response.getBody(), "contents");
    }

    @Test
    @DisplayName("요청한 포스트 아이디로 저장된 포스트를 찾아서 응답객체로 반환한다")
    void findPostById() {
        //given
        Long findId = 1L;
        PostEntity post = PostEntity.builder()
                .title("test1")
                .body("contents")
                .build();
        post.setIdForTest(findId);
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        //when
        PostReadResponse response = postService.getPost(findId);

        //then
        Assertions.assertEquals(response.getId(), findId);
    }
    @Test
    @DisplayName("찾으려고 하는 포스트의 아이디가 존재하지 않아 예외가 발생한다")
    void findPostByNotFoundedId(){
        //given
        PostEntity post1 = PostEntity.builder()
                .title("test1")
                .body("contents")
                .build();
        PostEntity post2 = PostEntity.builder()
                .title("test2")
                .body("contents")
                .build();
        List<PostEntity> requestList = List.of(post1, post2);
        postRepository.saveAll(requestList);
        Long requestId = 3L;

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, () -> postService.getPost(requestId));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }


}