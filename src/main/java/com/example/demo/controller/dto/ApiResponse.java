package com.example.demo.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data){
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message){
        return new ApiResponse<>(status, message, null);
    }

    public static <T> ApiResponse<T> ok(T data){
        return new ApiResponse<>(HttpStatus.OK, "SUCCESS", data);
    }
    public static <T> ApiResponse<T> ok(){
        return new ApiResponse<>(HttpStatus.OK, "SUCCESS", null);
    }
}
