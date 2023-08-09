package com.example.demo.controller;

import com.example.demo.controller.dto.ApiResponse;
import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostCreateResponse> create(@Valid @RequestBody PostCreateRequest request){
        // TODO :: 파라미터에 memberDto 추가
       return ApiResponse.ok(postService.create(request));
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostReadResponse> getPost(@PathVariable Long postId){
        return ApiResponse.ok(postService.getPost(postId));
    }
}
