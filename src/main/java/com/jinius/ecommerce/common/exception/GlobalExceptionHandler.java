package com.jinius.ecommerce.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //유효성 검사에 걸린 경우
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(BindException e) {
        log.warn("Invalid data format: {}", e.getMessage());

        ValidationErrorResponse errors = new ValidationErrorResponse();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.addError(fieldName, message);
        });

        log.warn(errors.getErrors().toString());

        return ResponseEntity.badRequest().body(errors);
    }

    //파라미터 타입에 맞지 않는 데이터로 API 요청한 경우
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("Invalid data format: {}", e.getMessage());

        ValidationErrorResponse errors = new ValidationErrorResponse();

        errors.addError(e.getMessage().substring(e.getMessage().lastIndexOf(": ")+3, e.getMessage().lastIndexOf('"')), "유효하지 않은 파라미터입니다.");
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Invalid data format: {}", e.getMessage());

        ValidationErrorResponse errors = new ValidationErrorResponse();

        String errorMessage = ((JsonMappingException) e.getCause()).getPathReference();
        int startIndex = errorMessage.lastIndexOf('[');
        int endIndex = errorMessage.lastIndexOf(']');
        String fieldName = errorMessage.substring(startIndex+2, endIndex-1);

        errors.addError(fieldName, "유효하지 않은 형식입니다.");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ValidationErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("Invalid data format: {}", e.getMessage());

        ValidationErrorResponse errors = new ValidationErrorResponse();

        errors.addError(
                e.getMessage().substring(e.getMessage().lastIndexOf(": ")+3, e.getMessage().lastIndexOf('"')),
                "유효하지 않은 파라미터입니다."
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = EcommerceException.class)
    public ResponseEntity<ErrorResponse> handleException(EcommerceException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(value = LockException.class)
    public ResponseEntity<ErrorResponse> handleException(LockException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse("ERR-00","서버 오류. 관리자에게 문의해주세요."));
    }
}
