package com.akdong.we.invitation.domain.formal;

import com.akdong.we.invitation.domain.BirthOrder;
import com.akdong.we.invitation.domain.FormalInvitationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    private String lastName;
    private String firstName;
    private BirthOrder birthOrder;

    private String fatherLastName;
    private String fatherFirstName;

    private String motherLastName;
    private String motherFirstName;


    @Schema(hidden = true)
    public void setGroomAttribute(FormalInvitationEntity invitation)
    {
        PersonDto groom = this;
        PersonDto bride = this;

        invitation.setGroom_last_name(groom.getLastName());
        invitation.setGroom_first_name(groom.getFirstName());
        invitation.setGroom_birth_order(groom.getBirthOrder());

        invitation.setGroom_father_first_name(groom.getFatherFirstName());
        invitation.setGroom_father_last_name(groom.getFatherLastName());

        invitation.setGroom_mother_last_name(groom.getMotherLastName());
        invitation.setGroom_mother_first_name(groom.getMotherFirstName());
    }

    @Schema(hidden = true)
    public void setBrideAttribute(FormalInvitationEntity invitation)
    {
        PersonDto bride = this;

        invitation.setBride_last_name(bride.getLastName());
        invitation.setBride_first_name(bride.getFirstName());
        invitation.setBride_birth_order(bride.getBirthOrder());

        invitation.setBride_father_first_name(bride.getFatherFirstName());
        invitation.setBride_father_last_name(bride.getFatherLastName());

        invitation.setBride_mother_last_name(bride.getMotherLastName());
        invitation.setBride_mother_first_name(bride.getMotherFirstName());
    }
}
