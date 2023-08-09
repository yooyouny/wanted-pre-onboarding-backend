package com.example.demo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResponse {
    private String token;

    @Builder
    private MemberLoginResponse(String token){
        this.token = token;
    }
}
