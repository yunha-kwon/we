package com.akdong.we.ledger.repository;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.ledger.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LedgerRepository  extends JpaRepository<Ledger, Long> {
    Optional<Ledger> findByCouple(Couple couple);
}
