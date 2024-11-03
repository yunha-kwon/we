package com.akdong.we.ledger.response;

import com.akdong.we.couple.response.CoupleInfo;
import com.akdong.we.ledger.entity.Ledger;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LedgerInfo {
    @Schema(description = "장부 ID")
    private final Long id;

    @Schema(description = "커플 정보")
    private final CoupleInfo coupleInfo;

    @Schema(description = "qr 코드 url")
    private final String qrCodeUrl;

    public static LedgerInfo of(Ledger ledger){
        return builder()
                .id(ledger.getId())
                .coupleInfo(CoupleInfo.of(ledger.getCouple()))
                .qrCodeUrl(ledger.getQrCodeUrl())
                .build();
    }


}
