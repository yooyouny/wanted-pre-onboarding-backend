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
    private LocalDateTime registeredAt;

    @Builder
    private MemberRegisterResponse(Long id, String email, LocalDateTime registeredAt){
        this.id = id;
        this.email = email;
        this.registeredAt = registeredAt;
    }
    public static MemberRegisterResponse fromEntity(MemberEntity entity){
        return MemberRegisterResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .registeredAt(entity.getCreatedDateTIme())
                .build();
    }
}
