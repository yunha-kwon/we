package com.akdong.we.member.response;

import com.akdong.we.couple.response.CoupleInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MemberAndTokensResponse {
    private MemberInfo memberInfo;
    private Map<String, String> tokens;
}
