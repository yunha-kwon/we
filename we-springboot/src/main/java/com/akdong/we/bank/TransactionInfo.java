package com.akdong.we.bank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfo {
    @Schema(description = "거래 고유 번호", example = "59")
    private String transactionUniqueNo;

    @Schema(description = "거래일자", example = "20240401")
    private String transactionDate;

    @Schema(description = "거래시각", example = "102447")
    private String transactionTime;

    @Schema(description = "입금출금구분", example = "1")
    private String transactionType;

    @Schema(description = "입금출금구분명", example = "입금")
    private String transactionTypeName;

    @Schema(description = "거래계좌번호", example = "12345678901234")
    private String transactionAccountNo;

    @Schema(description = "거래금액", example = "100000")
    private String transactionBalance;

    @Schema(description = "거래후잔액", example = "999000000")
    private String transactionAfterBalance;

    @Schema(description = "거래요약내용", example = "(수시입출금) : 출금")
    private String transactionSummary;

    @Schema(description = "거래 메모", example = "")
    private String transactionMemo;

    @Schema(description = "송금한 사람 이름" , example = "박하객")
    private String transactionUserName;
}
