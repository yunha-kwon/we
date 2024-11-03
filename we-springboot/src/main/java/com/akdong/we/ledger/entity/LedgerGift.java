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
public class LedgerGift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ledgerId")
    private Ledger ledger;

    @ManyToOne
    @JoinColumn(name = "giftId")
    private Gift gift;

}
