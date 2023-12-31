package com.example.demo.controller;

import com.example.demo.controller.dto.ApiResponse;
import com.example.demo.controller.dto.request.PostCreateRequest;
import com.example.demo.controller.dto.request.PostModifyRequest;
import com.example.demo.controller.dto.response.PostCreateResponse;
import com.example.demo.controller.dto.response.PostReadResponse;
import com.example.demo.entity.MemberDetails;
import com.example.demo.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostCreateResponse> create(@Valid @RequestBody PostCreateRequest request,
                                                  @AuthenticationPrincipal MemberDetails member){
       return ApiResponse.ok(postService.create(request, member.getUsername()));
    }
    @GetMapping("/{postId}")
    public ApiResponse<PostReadResponse> getPost(@PathVariable Long postId){
        return ApiResponse.ok(postService.getPost(postId));
    }
    @GetMapping
    public ApiResponse<Page<PostReadResponse>> getAllPost(@RequestParam(defaultValue = "0") int pageNo,
                                                          @RequestParam(defaultValue = "5") int size){
        return ApiResponse.ok(postService.getAllPost(pageNo, size));
    }
    @PutMapping("/{postId}")
    public ApiResponse<PostReadResponse> modify(@PathVariable Long postId,
                                                @Valid @RequestBody PostModifyRequest request,
                                                @AuthenticationPrincipal MemberDetails member){
        return ApiResponse.ok(postService.modify(postId, request, member.getUsername()));
    }
    @DeleteMapping("{postId}")
    public ApiResponse<Void> delete(@PathVariable Long postId,
                                    @AuthenticationPrincipal MemberDetails member){
        postService.delete(postId, member.getUsername());
        return ApiResponse.ok();
    }
}
