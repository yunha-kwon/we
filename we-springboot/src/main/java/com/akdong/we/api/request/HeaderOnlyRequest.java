package com.akdong.we.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HeaderOnlyRequest {
    @JsonProperty("Header")
    private CommonRequestHeader Header;
}
