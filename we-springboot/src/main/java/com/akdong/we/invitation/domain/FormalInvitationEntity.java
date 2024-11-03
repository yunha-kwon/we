package com.akdong.we.invitation.domain;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.file.domain.FileDto;
import com.akdong.we.invitation.domain.formal.GreetingsDto;
import com.akdong.we.invitation.domain.formal.MetaInfo;
import com.akdong.we.invitation.domain.formal.PersonDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Table(name="formal_invitation")
public class FormalInvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invitation_id;

    @Column
    private String title;

    @Column
    private String url;

    @Column
    private long couple_id;

    //신랑 측 정보
    @Column
    private String groom_last_name;

    @Column
    private String groom_first_name;

    @Enumerated(EnumType.STRING)
    @Column
    private BirthOrder groom_birth_order;

    //신랑 부모 정보
    @Column
    private String groom_father_last_name;

    @Column
    private String groom_father_first_name;

    @Column
    private String groom_mother_last_name;

    @Column
    private String groom_mother_first_name;

    //신부 측 정보
    @Column
    private String bride_last_name;

    @Column
    private String bride_first_name;

    @Enumerated(EnumType.STRING)
    @Column
    private BirthOrder bride_birth_order;

    //신부 부모 정보
    @Column
    private String bride_father_last_name;

    @Column
    private String bride_father_first_name;

    @Column
    private String bride_mother_last_name;

    @Column
    private String bride_mother_first_name;

    //인사말
    @Column
    private String greetings;

    //날짜
    @Column
    private String date;

    @Enumerated(EnumType.STRING)
    @Column
    private TIMEZONE timezone;

    @Column
    private int hour;

    @Column
    private int minute;

    //주소
    @Column
    private String address;

    @Column
    private String address_detail;

    @Column
    private String wedding_hall;

    @Column
    private double longtitude;

    @Column
    private double latitude;
    public FormalInvitationDto asDto(String account,String ownerName, String bankName,Long ledgerId) {
        return FormalInvitationDto.builder()

                .invitationId(invitation_id)
                .coupleId(couple_id)
                .coupleAccount(account)
                .coupleAccountOwner(ownerName)
                .coupleBankName(bankName)

                .ledgerId(ledgerId)

                .title(title)
                .url(url)

                .groomLastName(groom_last_name)
                .groomFirstName(groom_first_name)
                .groomBirthOrder(groom_birth_order)

                .groomFatherLastName(groom_father_last_name)
                .groomFatherFirstName(groom_father_first_name)

                .groomMotherLastName(groom_mother_last_name)
                .groomMotherFirstName(groom_mother_first_name)

                .brideLastName(bride_last_name)
                .brideFirstName(bride_first_name)
                .brideBirthOrder(bride_birth_order)

                .brideFatherLastName(bride_father_last_name)
                .brideFatherFirstName(bride_father_first_name)

                .brideMotherLastName(bride_mother_last_name)
                .brideMotherFirstName(bride_mother_first_name)

                .greetings(greetings)
                .date(date)
                .timezone(timezone)
                .hour(hour)
                .minute(minute)

                .address(address)
                .addressDetail(address_detail)
                .weddingHall(wedding_hall)

                .latitude(latitude)
                .longitude(longtitude)
                .build();
    }

    public FormalInvitationDto asDto(){
        return asDto(null,null,null,null);
    }

    public PersonDto asBrideDto()
    {
        return PersonDto
            .builder()
            .lastName(bride_last_name)
            .firstName(bride_first_name)
            .birthOrder(bride_birth_order)
            .fatherLastName(bride_father_last_name)
            .fatherFirstName(bride_father_last_name)
            .motherLastName(bride_mother_last_name)
            .motherFirstName(bride_mother_first_name)
            .build();
    }

    public PersonDto asGroomDto()
    {
        return PersonDto
                .builder()
                .lastName(groom_last_name)
                .firstName(groom_first_name)
                .birthOrder(groom_birth_order)
                .fatherLastName(groom_father_last_name)
                .fatherFirstName(groom_father_last_name)
                .motherLastName(groom_mother_last_name)
                .motherFirstName(groom_mother_first_name)
                .build();
    }

    public FileDto asFileDto()
    {
        return FileDto.builder()
                .url(url)
                .build();
    }

    public MetaInfo asMetaInfoDto()
    {
        return MetaInfo
                .builder()
                .date(date)
                .timezone(timezone)
                .hour(hour)
                .minute(minute)
                .address(address)
                .address_detail(address_detail)
                .wedding_hall(wedding_hall)
                .latitude(latitude)
                .longitude(longtitude)
                .build();
    }

    public GreetingsDto asGreetingDto()
    {
        return GreetingsDto.builder()
                .greetings(greetings)
                .build();
    }
}
