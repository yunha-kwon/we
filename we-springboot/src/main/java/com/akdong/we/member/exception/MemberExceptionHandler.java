package com.akdong.we.member.exception;

import com.akdong.we.common.dto.ErrorResponse;
import com.akdong.we.member.exception.auth.AuthException;
import com.akdong.we.member.exception.email.EmailException;
import com.akdong.we.member.exception.member.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(1)
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> MemberExceptionHandler1(MemberException e){
        log.error("MemberException occurred, message={}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse<>(e.getErrorCode().getDefaultMessage(), Map.of()), e.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> AuthExceptionHandler(AuthException e){
        log.error("AuthException occurred, message={}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse<>(e.getErrorCode().getDefaultMessage(), Map.of()), e.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<?> EmailExceptionHandler(EmailException e){
        log.error("EmailException occurred, message={}", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse<>(e.getErrorCode().getDefaultMessage(), Map.of()), e.getErrorCode().getHttpStatus()
        );
    }

}
