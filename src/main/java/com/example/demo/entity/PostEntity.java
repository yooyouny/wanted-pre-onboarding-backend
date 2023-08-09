package com.example.demo.entity;

import com.example.demo.controller.dto.request.PostCreateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    private LocalDateTime deltedAt;

    private boolean isDeleted;

    @Builder
    private PostEntity(String title, String body, MemberEntity member) {
        this.title = title;
        this.body = body;
        this.member = member;
        this.isDeleted = false;
    }

    public static PostEntity of(PostCreateRequest request, MemberEntity member){
        return PostEntity.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .member(member)
                .build();
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
}
