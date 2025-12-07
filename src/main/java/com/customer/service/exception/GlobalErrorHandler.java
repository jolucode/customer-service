package com.customer.service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ResponseEntity<String>> handleNotFound(CustomerNotFoundException ex) {
        return Mono.just(ResponseEntity.notFound().build());
    }
}
