package com.akdong.we.member.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResetUrlResponse {
    private String url;
}
