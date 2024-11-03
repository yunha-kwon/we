package com.akdong.we.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckAuthRequest {
    @JsonProperty("Header")
    private CommonRequestHeader Header;
    private String accountNo;
    private String authText;
    private String authCode;
}
