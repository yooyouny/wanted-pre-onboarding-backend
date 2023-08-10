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
    private LocalDateTime registeredAt;

    @Builder
    private PostCreateResponse(Long id, String title, String body, String email, LocalDateTime registeredAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public static PostCreateResponse fromEntity(PostEntity entity){
        return PostCreateResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .email(entity.getMember().getEmail())
                .registeredAt(entity.getCreatedDateTIme())
                .build();
    }

}
