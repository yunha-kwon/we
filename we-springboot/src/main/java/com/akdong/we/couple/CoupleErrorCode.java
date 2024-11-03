package com.akdong.we.couple;


import com.akdong.we.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CoupleErrorCode implements ErrorCode {
    COUPLE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "커플을 찾을 수 없습니다."),
    ACCOUNT_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "커플 계좌를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
