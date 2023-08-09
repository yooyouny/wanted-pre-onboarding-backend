package com.example.demo.controller.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRegisterRequest {

    @NotBlank(message = "이메일은 필수 입니다")
    @Pattern(regexp = ".*@.*", message = "이메일에 '@' 문자가 포함되어 있어야 합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;

    @Builder
    private MemberRegisterRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}
