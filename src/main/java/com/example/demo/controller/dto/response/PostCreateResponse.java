package com.example.demo.controller.dto.response;

import com.example.demo.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostCreateResponse {

    private Long id;
    private String title;
    private String body;
    private String email;
    private LocalDateTime registerAt;

    @Builder
    private PostCreateResponse(Long id, String title, String body, String email, LocalDateTime registerAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.email = email;
        this.registerAt = registerAt;
    }

    public static PostCreateResponse fromEntity(PostEntity entity){
        return PostCreateResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .email(entity.getMember().getEmail())
                .registerAt(entity.getCreatedDateTIme())
                .build();
    }

}
