package com.example.demo.entity;

import com.example.demo.controller.dto.request.MemberRegisterRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    private String email;

    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostEntity> postList  = new ArrayList<PostEntity>();

    @Builder
    private MemberEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberEntity fromDto(MemberRegisterRequest request){
        return MemberEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
