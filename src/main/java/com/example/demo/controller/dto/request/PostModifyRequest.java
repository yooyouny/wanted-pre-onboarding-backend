package com.example.demo.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostModifyRequest {

    @NotBlank(message = "게시글 제목은 필수입니다")
    private String title;

    @NotBlank(message = "게시글 내용은 필수입니다")
    private String body;

    @Builder
    private PostModifyRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
