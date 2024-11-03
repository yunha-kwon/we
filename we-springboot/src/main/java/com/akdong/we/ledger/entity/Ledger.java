package com.akdong.we.ledger.entity;

import com.akdong.we.couple.entity.Couple;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "coupleId")
    private Couple couple;

    private String qrCodeUrl;
}
