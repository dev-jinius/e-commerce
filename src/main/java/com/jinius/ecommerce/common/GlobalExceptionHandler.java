package com.jinius.ecommerce.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EcommerceException.class)
    public ResponseEntity<ErrorResponse> handleException(EcommerceException e) {
        log.warn(e.getErrorCode().getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("ERR-00","서버 오류"));
    }
}
