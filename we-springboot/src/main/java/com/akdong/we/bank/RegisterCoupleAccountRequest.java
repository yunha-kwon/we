package com.akdong.we.bank;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterCoupleAccountRequest {
    private String accountNo;
    private String bankName;
}
