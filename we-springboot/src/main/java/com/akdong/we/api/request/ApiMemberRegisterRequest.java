package com.akdong.we.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiMemberRegisterRequest {
    private String apiKey;
    private String userId;
}
