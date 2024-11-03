package com.akdong.we.member.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatedMemberInfoRequest {
    private String password;
    private String nickname;
}
