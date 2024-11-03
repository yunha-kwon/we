package com.akdong.we.couple.entity;

import com.akdong.we.ledger.entity.Ledger;
import com.akdong.we.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member1Id")
    private Member member1;

    @OneToOne
    @JoinColumn(name = "member2Id")
    private Member member2;

    @Column(name="bankbookCreated", nullable = false)
    private boolean bankbookCreated;

    @Column(name="accountNumber")
    private String accountNumber;

    @Column(name="accountOwnerId")
    private Long accountOwnerId;

    @Column(name="accountBankName")
    private String accountBankName;

    @Column(name="ledgerCreated", nullable = false)
    private boolean ledgerCreated;

    @OneToOne(mappedBy = "couple", cascade = CascadeType.ALL)
    private Ledger ledger;

    @Column(name="weddingDate")
    private String weddingDate;

}
