package com.akdong.we.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(builderMethodName = "customBuilder")
public class CommonRequestHeader {
    private static final String INSTITUTION_CODE = "00100";
    private static final String FINTECH_APP_NO = "001";

    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;
    private String userKey;

    // 커스텀 빌더 메서드
    public static CommonRequestHeaderBuilder customBuilder() {
        // 고정값을 기본으로 설정하고 빌더 생성
        return new CommonRequestHeaderBuilder()
                .institutionCode(INSTITUTION_CODE)
                .fintechAppNo(FINTECH_APP_NO)
                .institutionTransactionUniqueNo(UniqueTransactionGenerator.generateRandomTransactionNo());
    }
}
