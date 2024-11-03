package com.akdong.we.invitation.domain.formal;

import com.akdong.we.invitation.domain.FormalInvitationEntity;
import com.akdong.we.invitation.domain.TIMEZONE;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaInfo {

    private String date;
    private TIMEZONE timezone;
    private int hour;
    private int minute;

    private String address;
    private String address_detail;
    private String wedding_hall;

    private double latitude;
    private double longitude;

    @Schema(hidden = true)
    public void setAttribute(FormalInvitationEntity invitation)
    {
        MetaInfo metaInfo = this;

        invitation.setDate(metaInfo.getDate());
        invitation.setTimezone(metaInfo.getTimezone());
        invitation.setHour(metaInfo.getHour());
        invitation.setMinute(metaInfo.getMinute());

        invitation.setAddress(metaInfo.getAddress());
        invitation.setAddress_detail(metaInfo.getAddress_detail());
        invitation.setWedding_hall(metaInfo.getWedding_hall());

        invitation.setLatitude(metaInfo.getLatitude());
        invitation.setLongtitude(metaInfo.getLongitude());
    }
}
