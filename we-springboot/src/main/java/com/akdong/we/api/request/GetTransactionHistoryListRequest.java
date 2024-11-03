package com.akdong.we.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetTransactionHistoryListRequest {
    @JsonProperty("Header")
    CommonRequestHeader Header;
    private String accountNo;
    private String startDate;
    private String endDate;
    private String transactionType;
    private String orderByType;
}
