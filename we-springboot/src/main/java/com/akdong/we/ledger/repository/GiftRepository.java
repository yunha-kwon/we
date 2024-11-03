package com.akdong.we.ledger.repository;

import com.akdong.we.ledger.entity.Gift;
import com.akdong.we.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findByMemberAndMealTicketUrlIsNotNull(Member member);
}
