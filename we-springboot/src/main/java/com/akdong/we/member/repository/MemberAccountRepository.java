package com.akdong.we.member.repository;

import com.akdong.we.member.entity.Member;
import com.akdong.we.member.entity.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
    List<MemberAccount> findByMember(Member member);
    Optional<MemberAccount> findByAccountNo(String accountNo);
}
