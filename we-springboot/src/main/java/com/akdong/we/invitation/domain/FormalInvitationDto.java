package com.akdong.we.invitation.domain;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.invitation.service.FormalInvitationService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormalInvitationDto {

    private long invitationId;

    private long coupleId;
    private String coupleAccountOwner;
    private String coupleBankName;
    private String coupleAccount;

    private Long ledgerId;

    private String title;

    // 초대장 URL
    private String url;

    // 신랑 측 정보
    private String groomLastName;
    private String groomFirstName;
    private BirthOrder groomBirthOrder;

    // 신랑 부모 정보
    private String groomFatherLastName;
    private String groomFatherFirstName;
    private String groomMotherLastName;
    private String groomMotherFirstName;

    // 신부 측 정보
    private String brideLastName;
    private String brideFirstName;
    private BirthOrder brideBirthOrder;

    // 신부 부모 정보
    private String brideFatherLastName;
    private String brideFatherFirstName;
    private String brideMotherLastName;
    private String brideMotherFirstName;

    // 인사말
    private String greetings;

    // 날짜 및 시간 정보
    private String date;
    private TIMEZONE timezone;
    private int hour;
    private int minute;

    // 주소
    private String address;
    private String addressDetail;
    private String weddingHall;

    private double longitude;
    private double latitude;

    @Schema(hidden = true)
    public FormalInvitationEntity asEntity(){
        return FormalInvitationEntity.builder()
                .title(title)
                .invitation_id(invitationId)
                .couple_id(coupleId)
                .url(url)

                .groom_last_name(groomLastName)
                .groom_first_name(groomFirstName)
                .groom_birth_order(groomBirthOrder)

                .groom_father_last_name(groomFatherLastName)
                .groom_father_first_name(groomFatherFirstName)

                .groom_mother_first_name(groomMotherFirstName)
                .groom_mother_last_name(groomMotherLastName)

                .bride_last_name(brideLastName)
                .bride_first_name(brideFirstName)
                .bride_birth_order(brideBirthOrder)

                .bride_father_last_name(brideFatherLastName)
                .bride_father_first_name(brideFatherFirstName)

                .bride_mother_last_name(brideMotherLastName)
                .bride_mother_first_name(brideMotherFirstName)

                .greetings(greetings)
                .date(date)
                .timezone(timezone)
                .hour(hour)
                .minute(minute)

                .address(address)
                .address_detail(addressDetail)
                .wedding_hall(weddingHall)

                .build();
    }
}