package com.akdong.we.ledger.response;

import com.akdong.we.ledger.entity.Gift;
import com.akdong.we.member.response.MemberInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GiftInfo {
    @Schema(description = "축의금 ID")
    private final Long id;

    @Schema(description = "멤버 정보")
    private final MemberInfo memberInfo;

    @Schema(description = "신부측 여부")
    private final Boolean isBride;

    @Schema(description = "축의금 금액")
    private final Long charge;

    @Schema(description = "메시지")
    private final String message;

    @Schema(description = "식권 URL")
    private final String mealTicketUrl;

    public static GiftInfo of(Gift gift){
        return builder()
                .id(gift.getId())
                .memberInfo(MemberInfo.of(gift.getMember()))
                .isBride(gift.getIsBride())
                .charge(gift.getCharge())
                .message(gift.getMessage())
                .mealTicketUrl(gift.getMealTicketUrl())
                .build();
    }
}
