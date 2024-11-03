package com.akdong.we.bank;

import com.akdong.we.api.request.CommonRequestHeader;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class TransferRequest {

    @Schema(hidden = true)
    @JsonProperty("Header")
    private CommonRequestHeader Header; // Header

    private Long ledgerId;

    private String depositAccountNo; // 입금 계좌 번호

    private Long transactionBalance; // 거래 금액

    private String withdrawalAccountNo; // 출금 계좌번호

    private Boolean isBride; // 신랑 신부 여부

    private String pin; // 간편 비밀번호
}
