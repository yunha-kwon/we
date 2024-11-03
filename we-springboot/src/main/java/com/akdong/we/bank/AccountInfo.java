package com.akdong.we.bank;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    private String bankCode;
    private String bankName;
    private String userName;
    private String accountNo;
    private String accountName;
    private String accountTypeCode;
    private String accountTypeName;
    private String accountCreatedDate;
    private String accountExpiryDate;
    private String dailyTransferLimit;
    private String oneTimeTransferLimit;
    private String accountBalance;
    private String lastTransactionDate;
    private String currency;
    private String accountInfo; // "대표계좌", "커플계좌", "일반계좌"
}