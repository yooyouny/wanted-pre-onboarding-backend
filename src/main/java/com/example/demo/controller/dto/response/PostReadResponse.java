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
    private LocalDateTime registeredAt;
    private LocalDateTime modifiedAt;

    @Builder
    private PostReadResponse(Long id, String title, String body, String email, LocalDateTime registeredAt, LocalDateTime modifiedAt){
        this.id = id;
        this.title = title;
        this.body = body;
        this.email = email;
        this.registeredAt = registeredAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostReadResponse fromEntity(PostEntity entity){
        return PostReadResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .body(entity.getBody())
                .email(entity.getMember().getEmail())
                .registeredAt(entity.getCreatedDateTIme())
                .modifiedAt(entity.getModifiedDateTime())
                .build();
    }

}
