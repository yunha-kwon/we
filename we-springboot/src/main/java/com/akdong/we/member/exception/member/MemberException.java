package com.akdong.we.member.exception.member;

import com.akdong.we.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException  {
    private final ErrorCode errorCode;
    private final String message;

    public MemberException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage());
    }

    public MemberException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
