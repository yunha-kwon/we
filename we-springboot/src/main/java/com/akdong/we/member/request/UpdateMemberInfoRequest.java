package com.akdong.we.member.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMemberInfoRequest {
    private String password;
    private String nickname;
    private Long imageId;
}
