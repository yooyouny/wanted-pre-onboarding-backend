package com.example.demo.unit.repository;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.entity.CustomMemberDetails;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest// @Trsactional 포함으로 자동 롤백
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// 내장 datasource를 사용하지 않음
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void saveNewPost(){
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("test1")
                .body("contents")
                .build();

        MemberEntity member = MemberEntity.builder()
                .email("email@")
                .password("password")
                .build();

        //when
        PostEntity savedPost = postRepository.save(PostEntity.of(request, member));

        //then
        Assertions.assertEquals(savedPost.getId(), 1);
        Assertions.assertEquals(savedPost.getTitle(), "test1");
    }

    @Test
    void findPostById(){
        //given
        PostEntity requestPost = PostEntity.builder()
                .title("test1")
                .body("contents")
                .build();
        PostEntity savedPost = postRepository.save(requestPost);

        //when
        Assertions.assertDoesNotThrow(() -> postRepository.findById(savedPost.getId()));
    }

}
