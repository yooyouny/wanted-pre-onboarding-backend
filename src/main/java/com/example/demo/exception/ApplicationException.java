package com.example.demo.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApplicationException extends RuntimeException{

    private HttpStatus status;
    private String message;

    @Builder
    private ApplicationException(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

}
