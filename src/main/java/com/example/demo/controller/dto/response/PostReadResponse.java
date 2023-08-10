package com.example.demo.controller.dto.response;

import com.example.demo.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostReadResponse {

    private Long id;
    private String title;
    private String body;
    private String email;
    private LocalDateTime registedAt;
    private LocalDateTime modifiedAt;

    @Builder
    private PostReadResponse(Long id, String title, String body, String email, LocalDateTime registedAt, LocalDateTime modifiedAt){
        this.id = id;
        this.title = title;
        this.body = body;
        this.email = email;
        this.registedAt = registedAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostReadResponse fromEntity(PostEntity entity){
        return PostReadResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .email(entity.getMember().getEmail())
                .registedAt(entity.getCreatedDateTIme())
                .modifiedAt(entity.getModifiedDateTime())
                .build();
    }

}
