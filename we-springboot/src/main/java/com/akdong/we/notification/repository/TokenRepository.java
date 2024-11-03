package com.akdong.we.notification.repository;

import com.akdong.we.invitation.domain.FormalInvitationEntity;
import com.akdong.we.notification.domain.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("SELECT t FROM TokenEntity t WHERE t.user_id = :user_id")
    TokenEntity findTokenByUserId(@Param("user_id") long user_id);
}
