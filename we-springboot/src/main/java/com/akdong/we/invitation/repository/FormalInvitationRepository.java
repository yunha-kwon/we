package com.akdong.we.invitation.repository;

import com.akdong.we.invitation.domain.FormalInvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormalInvitationRepository extends JpaRepository<FormalInvitationEntity, Long> {
    @Query("SELECT c FROM FormalInvitationEntity c WHERE c.couple_id = :couple_id")
    List<FormalInvitationEntity> findFormalInvitationByCoupleId(@Param("couple_id") long couple_id);
}
