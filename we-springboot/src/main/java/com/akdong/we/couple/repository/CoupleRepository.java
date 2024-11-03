package com.akdong.we.couple.repository;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Long> {
    @Query("SELECT c FROM Couple c WHERE c.member1 = :member OR c.member2 = :member")
    Optional<Couple> findByMember(@Param("member") Member member);


}
