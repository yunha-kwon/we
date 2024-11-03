package com.akdong.we.bank;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckAuthUserRequest {
    private String accountNo;
    private String authText;
    private String authCode;
}
