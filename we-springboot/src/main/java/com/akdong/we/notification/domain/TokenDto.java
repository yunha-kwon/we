package com.akdong.we.notification.domain;

import lombok.*;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
    private long user_id;
    private String token;

    public TokenEntity asEntity()
    {
        return TokenEntity
                .builder()
                .user_id(user_id)
                .token(token)
                .build();
    }
}
