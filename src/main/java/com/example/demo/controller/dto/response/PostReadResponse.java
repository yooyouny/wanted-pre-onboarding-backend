package com.example.demo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostReadResponse {

    private Long id;
    private String title;
    private String body;

    @Builder
    private PostReadResponse(Long id, String title, String body){
        this.id = id;
        this.title = title;
        this.body = body;
    }

}
