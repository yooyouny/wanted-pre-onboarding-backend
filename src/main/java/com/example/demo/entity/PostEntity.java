package com.example.demo.entity;

import com.example.demo.controller.dto.request.PostCreateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //TODO :: 생성자에 Member 추가
    @Builder
    private PostEntity(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public static PostEntity of(PostCreateRequest request){
        return PostEntity.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .build();
    }

    public void setIdForTest(Long id){
        this.id = id;
    }
}
