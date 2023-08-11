package com.example.demo.unit.service;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.request.PostModifyRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.exception.ApplicationException;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;

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
        MemberEntity writer = createMember("email@");

        PostEntity savedPost = PostEntity.of(requestPost, writer);
        savedPost.setIdForTest(1L);
        given(postRepository.save(any())).willReturn(savedPost);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(writer));

        //when
        PostCreateResponse response = postService.create(requestPost, writer.getEmail());

        //then
        Assertions.assertEquals(response.getId(), 1L);
        Assertions.assertEquals(response.getTitle(), "test");
        Assertions.assertEquals(response.getBody(), "contents");
    }

    @Test
    @DisplayName("요청한 포스트 아이디로 저장된 포스트를 찾아서 응답객체로 반환한다")
    void findPostById() {
        //given
        Long requestId = 1L;
        MemberEntity writer = createMember("email@");
        PostEntity post = createPost("test1", writer);
        post.setIdForTest(requestId);
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        //when
        PostReadResponse response = postService.getPost(requestId);

        //then
        Assertions.assertEquals(response.getId(), requestId);
    }
    @Test
    @DisplayName("찾으려고 하는 포스트의 아이디가 존재하지 않아 예외가 발생한다")
    void findPostByNotFoundId(){
        //given
        MemberEntity writer = createMember("email@");
        PostEntity post1 = createPost("test1", writer);
        PostEntity post2 = createPost("test2", writer);

        List<PostEntity> requestList = List.of(post1, post2);
        postRepository.saveAll(requestList);
        Long requestId = 4L;
        given(postRepository.findById(requestId)).willReturn(Optional.empty());

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, () -> postService.getPost(requestId));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("모든 포스트를 Page 객체에 담아 응답객체로 반환한다")
    void findByAllPost(){
        //given
        MemberEntity writer = createMember("writer@");
        PostEntity post1 = createPost("test1", writer);
        PostEntity post2 = createPost("test2", writer);
        PostEntity post3 = createPost("test3", writer);
        PostEntity post4 = createPost("test4", writer);
        PostEntity post5 = createPost("test5", writer);
        List<PostEntity> entities = List.of(post1, post2, post3, post4, post5);
        given(postRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(entities));

        //when
        Page<PostReadResponse> responsePage = postService.getAllPost(0, 5);
        List<PostReadResponse> responses = responsePage.getContent();

        // Then
        assertThat(responses).hasSize(5)
                .extracting("title", "email")
                .containsExactlyInAnyOrder(
                        tuple("test1", "writer@"),
                        tuple("test2", "writer@"),
                        tuple("test3", "writer@"),
                        tuple("test4", "writer@"),
                        tuple("test5", "writer@")
                );
    }

    @Test
    @DisplayName("요청한 포스트 아이디로 저장된 포스트를 찾아서 요청한 내용으로 수정한다")
    void modifyPostByPostId(){
        //given
        MemberEntity requestMember = createMember("email@");
        PostEntity savedPost = createPost("title", requestMember);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(requestMember));
        given(postRepository.findById(any())).willReturn(Optional.of(savedPost));


        Long requestPostId = 1L;
        PostModifyRequest request = PostModifyRequest.builder()
                .title("modify")
                .body("modified")
                .build();
        savedPost.modifyPost(request);
        savedPost.setIdForTest(requestPostId);
        given(postRepository.saveAndFlush(any())).willReturn(savedPost);

        //when
        PostReadResponse response = postService.modify(requestPostId, request, requestMember.getEmail());

        //then
        Assertions.assertEquals(response.getId(), 1L);
        Assertions.assertEquals(response.getTitle(), "modify");
        Assertions.assertEquals(response.getBody(), "modified");
    }

    @Test
    @DisplayName("포스트 수정자와 작성자가 일치하지 않아 예외가 발생한다")
    void modifyPostByNotAccessMember(){
        //given
        MemberEntity wrongMember = createMember("test@");
        MemberEntity writer = createMember("email@");
        wrongMember.setIdForTest(1L);
        wrongMember.setIdForTest(2L);
        PostEntity post = createPost("test1", writer);

        given(memberRepository.findByEmail(any())).willReturn(Optional.of(wrongMember));
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        Long requestPostId = 1L;
        PostModifyRequest request = PostModifyRequest.builder()
                .title("modify")
                .body("modified")
                .build();

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, () ->
                postService.modify(requestPostId, request, wrongMember.getEmail()));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.FORBIDDEN);
    }
    @Test
    @DisplayName("요청한 포스트 아이디로 포스트을 찾아 삭제한다")
    void deleteByPostId(){
        MemberEntity requestMember = createMember("email@");
        PostEntity savedPost = createPost("title", requestMember);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(requestMember));
        given(postRepository.findById(any())).willReturn(Optional.of(savedPost));

        Long requestPostId = 1L;

        Assertions.assertDoesNotThrow(() -> postService.delete(requestPostId, "email@"));
    }

    @Test
    @DisplayName("포스트 삭제 요청자와 작성자가 일치하지 않아 예외가 발생한다")
    void deletePostByNotAccessMember(){
        //given
        MemberEntity requestMember = createMember("test@");
        MemberEntity writer = createMember("email@");
        requestMember.setIdForTest(1L);
        writer.setIdForTest(2L);
        PostEntity post = createPost("test1", writer);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(requestMember));
        given(postRepository.findById(any())).willReturn(Optional.of(post));

        Long requestPostId = 1L;

        //when
        ApplicationException e = Assertions.assertThrows(ApplicationException.class, () ->
                postService.delete(requestPostId, requestMember.getEmail()));

        //then
        Assertions.assertEquals(e.getStatus(), HttpStatus.FORBIDDEN);
    }

    private MemberEntity createMember(String email){
        return MemberEntity.builder()
                .email(email)
                .password("password")
                .build();
    }
    private PostEntity createPost(String title, MemberEntity member){
        return PostEntity.builder()
                .title(title)
                .body("contents")
                .member(member)
                .build();
    }

}