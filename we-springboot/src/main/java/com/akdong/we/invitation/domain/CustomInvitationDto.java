package com.akdong.we.invitation.domain;

import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomInvitationDto {
    private long invitationId;
    private Map<String, Map<String,String>> tree;
}


