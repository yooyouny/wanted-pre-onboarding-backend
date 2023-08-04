package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이메일은 필수 입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostEntity> postList  = new ArrayList<PostEntity>();

    @Builder
    private MemberEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
