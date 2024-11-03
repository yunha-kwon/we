package com.akdong.we.common.exception;

import com.akdong.we.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(10)
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<?> handleCustomException(BusinessException ex) {
        log.error("BusinessException occurred, message={}", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse<>(
                        ex.getErrorCode().getDefaultMessage(),
                        Map.of()
                ),
                ex.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred", ex);

        Map<String, Object> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new ErrorResponse<>(
                        "유효하지 않은 입력입니다.",
                                errors)
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("Exception occurred", ex);

        return ResponseEntity.internalServerError()
                .body(new ErrorResponse<>(
                        "죄송합니다. 서버 내부에 오류가 발생했습니다.",
                        Map.of()
                ));
    }
}