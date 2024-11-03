package com.akdong.we.member.exception.auth;

import com.akdong.we.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public AuthException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage());
    }

    public AuthException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
