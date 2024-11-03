package com.akdong.we.ledger.entity;

import com.akdong.we.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    private Boolean isBride;

    private Long charge;

    private String message;

    private String mealTicketUrl; // 식권 URL
}
