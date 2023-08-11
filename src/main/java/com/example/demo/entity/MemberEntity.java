package com.example.demo.entity;

import com.example.demo.controller.dto.request.MemberLoginRequest;
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

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "member")
    private final List<PostEntity> postList  = new ArrayList<PostEntity>();

    @Builder
    private MemberEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static MemberEntity of(MemberRegisterRequest request){
        return MemberEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
    public static MemberEntity of(MemberLoginRequest request){
        return MemberEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public void setIdForTest(Long id){
        this.id = id;
    }

}
