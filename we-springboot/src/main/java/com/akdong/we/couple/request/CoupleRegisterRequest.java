package com.akdong.we.couple.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoupleRegisterRequest {
    @NotBlank(message = "커플 코드를 입력해주세요.")
    @Schema(description = "커플 코드", example = "123456")
    private String code;

    @Schema(description = "결혼식 날짜", example = "2024-10-02")
    private String weddingDate;
}
