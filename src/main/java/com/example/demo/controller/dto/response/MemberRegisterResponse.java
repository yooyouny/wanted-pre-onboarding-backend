package com.example.demo.controller.dto.response;

import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberRegisterResponse {

    private Long id;
    private String email;
    private String password;
    private LocalDateTime registedAt;

    @Builder
    private MemberRegisterResponse(Long id, String email, String password, LocalDateTime registedAt){
        this.id = id;
        this.email = email;
        this.password = password;
        this.registedAt = registedAt;
    }
    public static MemberRegisterResponse fromEntity(MemberEntity entity){
        return MemberRegisterResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .registedAt(entity.getCreatedDateTIme())
                .build();
    }
}
