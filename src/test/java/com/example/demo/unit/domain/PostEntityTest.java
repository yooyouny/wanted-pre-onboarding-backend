package com.example.demo.unit.domain;

import com.example.demo.controller.dto.request.PostModifyRequest;
import com.example.demo.entity.PostEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostEntityTest {
    @Test
    @DisplayName("포스트 수정 시 도메인에서 상태가 변경된다")
    void updatePost(){
        //given
        PostEntity postEntity = PostEntity.builder()
                .title("original title")
                .body("original body")
                .build();

        PostModifyRequest request = PostModifyRequest.builder()
                .title("updated title")
                .body("updated body")
                .build();

        //when
        postEntity.modifyPost(request);

        //then
        assertEquals("updated title", postEntity.getTitle());
        assertEquals("updated body", postEntity.getBody());
    }
    @Test
    @DisplayName("포스트 삭제 시 도메인에서 상태가 변경된다")
    void deletePost(){
        //given
        PostEntity postEntity = PostEntity.builder()
                .title("original title")
                .body("original body")
                .build();

        //when
        postEntity.deletePost();

        //then
        assertNotNull(postEntity.getDeletedAt());
        Assertions.assertTrue(postEntity.isDeleted());
    }
}
