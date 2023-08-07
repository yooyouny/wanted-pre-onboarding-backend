package com.example.demo.repository;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.entity.PostEntity;
import com.example.demo.post.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest// @Trsactional 포함으로 자동 롤백
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// 내장 datasource를 사용하지 않음
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("새 포스트를 저장한다")
    void saveNewPost(){
        //given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("test1")
                .body("contents")
                .build();

        //when
        PostEntity savedPost = postRepository.save(PostEntity.of(request));

        //then
        Assertions.assertEquals(savedPost.getId(), 1);
        Assertions.assertEquals(savedPost.getTitle(), "test1");
    }

    @Test
    @DisplayName("포스트의 아이디로 저장된 포스트를 찾는다")
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
