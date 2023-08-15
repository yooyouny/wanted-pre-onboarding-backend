package com.example.demo.unit.repository;

import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.PostEntity;
import com.example.demo.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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
        Assertions.assertEquals(savedPost.getMember().getEmail(), "email@");
    }

    @Test
    void findPostById(){
        //given
        PostEntity requestPost = createPost("test01");
        PostEntity savedPost = postRepository.save(requestPost);
        savedPost.setIdForTest(1L);

        //when then
        Assertions.assertDoesNotThrow(() -> postRepository.findById(savedPost.getId()));
        Assertions.assertEquals(savedPost.getId(), 1L);
        Assertions.assertEquals(savedPost.getTitle(), "test01");
    }

    @Test
    void findAll(){
        //given
        List<PostEntity> savedPost = List.of(createPost("test1")
                                    ,createPost("test2")
                                    ,createPost("test3")
                                    ,createPost("test4"));
        postRepository.saveAll(savedPost);
        Pageable pageable = PageRequest.of(0, 3);

        //when
        Page<PostEntity> response= postRepository.findAll(pageable);

        //then
        assertThat(response).hasSize(3)
                .extracting("title", "body")
                .containsExactlyInAnyOrder(
                        tuple("test1", "contents"),
                        tuple("test2", "contents"),
                        tuple("test3", "contents")
                );
    }
    private PostEntity createPost(String title){
        return PostEntity.builder()
                .title(title)
                .body("contents")
                .build();
    }

}
