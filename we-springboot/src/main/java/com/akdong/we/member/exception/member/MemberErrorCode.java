package com.akdong.we.member.exception.member;

import com.akdong.we.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    MEMBER_EMAIL_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일입니다."),
    MEMBER_EMAIL_EXIST_ERROR(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    MEMBER_EMAIL_NOT_EXIST_ERROR(HttpStatus.BAD_REQUEST, "존재하지 않은 이메일입니다."),
    MEMBER_LEAVED_ERROR(HttpStatus.BAD_REQUEST, "탈퇴한 이용자입니다."),
    API_REGISTER_ERROR(HttpStatus.BAD_REQUEST, "FIN OPEN API 회원가입 오류입니다."),
    API_MAKE_ACCOUNT_ERROR(HttpStatus.BAD_REQUEST, "FIN OPEN API 계좌생성 오류입니다."),
    API_DEPOSIT_ERROR(HttpStatus.BAD_REQUEST, "FIN OPEN API 입금 오류입니다."),
    COUPLE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "커플을 찾을 수 없습니다."),
    MEMBER_PIN_ERROR(HttpStatus.BAD_REQUEST, "잘못된 간편 비밀번호(핀번호)입니다."),
    ACCOUNT_MEMBER_ERROR(HttpStatus.BAD_REQUEST, "계좌에 해당하는 멤버 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
