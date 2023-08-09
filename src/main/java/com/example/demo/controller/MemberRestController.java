package com.example.demo.controller;

import com.example.demo.controller.dto.ApiResponse;
import com.example.demo.controller.dto.request.MemberRegisterRequest;
import com.example.demo.controller.dto.response.MemberRegisterResponse;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ApiResponse<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest request){
       return ApiResponse.ok(memberService.register(request));
    }

}
