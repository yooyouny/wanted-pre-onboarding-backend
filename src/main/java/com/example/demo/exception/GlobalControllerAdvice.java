package com.example.demo.exception;

import com.example.demo.controller.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationExceptionHandler(ApplicationException e){
        log.info("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getStatus())
                .body(ApiResponse.error(e.getStatus(),e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validExceptionHandler(MethodArgumentNotValidException e) {
        log.info("Error occurs {} in validExceptionHandler", e.toString());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

        return ResponseEntity.status(e.getStatusCode())
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(),errorMessages));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationExceptionHandler(RuntimeException e){
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }
}
