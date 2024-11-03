package com.akdong.we.ledger;

import com.akdong.we.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LedgerErrorCode implements ErrorCode {
    COUPLE_ALREADY_CREATED_ERROR(HttpStatus.BAD_REQUEST, "이미 장부를 개설했습니다."),
    LEDGER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "장부를 찾을 수 없습니다."),
    QR_GENERATE_FAILED_ERROR(HttpStatus.BAD_REQUEST, "QR코드 생성에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
