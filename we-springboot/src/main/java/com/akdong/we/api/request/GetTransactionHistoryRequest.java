package com.akdong.we.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetTransactionHistoryRequest {
    @JsonProperty("Header")
    CommonRequestHeader Header;
    private String accountNo;
    private String transactionUniqueNo;
}
