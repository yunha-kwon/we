package com.akdong.we.invitation.service;

import com.akdong.we.common.auth.MemberDetails;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.repository.CoupleRepository;
import com.akdong.we.file.domain.FileDto;
import com.akdong.we.invitation.domain.FormalInvitationDto;
import com.akdong.we.invitation.domain.FormalInvitationEntity;
import com.akdong.we.invitation.domain.formal.*;
import com.akdong.we.invitation.repository.FormalInvitationRepository;
import com.akdong.we.ledger.entity.Ledger;
import com.akdong.we.ledger.repository.LedgerRepository;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.repository.MemberRepository;
import com.akdong.we.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormalInvitationService {

    private final LedgerRepository ledgerRepository;
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final FormalInvitationRepository formalInvitationRepository;

    public EmptyFormalInvitation saveFormalInvitation(){
        FormalInvitationDto ret = formalInvitationRepository
                .save(FormalInvitationEntity.builder().couple_id(getCoupleIdFromJwt()).build())
                .asDto();

        return EmptyFormalInvitation.builder()
                .invitation_id(ret.getInvitationId())
                .build();
    }

    public FileDto updateFormalInvitationFile(String url, long id) {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        invitation.setUrl(url);

        return formalInvitationRepository
                .save(invitation)
                .asFileDto();
    }

    @Transactional
    public PersonDto updateFormalInvitationBride(long id, PersonDto bride) {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        bride.setBrideAttribute(invitation);

        return formalInvitationRepository
                .save(invitation)
                .asBrideDto();
    }

    public PersonDto updateFormalInvitationGroom(long id, PersonDto groom)
    {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        groom.setGroomAttribute(invitation);

        return formalInvitationRepository
                .save(invitation)
                .asGroomDto();
    }

    public GreetingsDto updateFormalInvitationGreetings(
            @PathVariable("invitationId") long id,
            GreetingsDto greetings
    )
    {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        invitation.setGreetings(greetings.getGreetings());

        return formalInvitationRepository
                .save(invitation)
                .asGreetingDto();
    }

    public MetaInfo updateFormalInvitationMetaInfo(long id, MetaInfo metaInfo)
    {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        metaInfo.setAttribute(invitation);

        return formalInvitationRepository
                .save(invitation)
                .asMetaInfoDto();
    }

    public FormalInvitationDto findFormalInvitationById(long id){
        var invitation = formalInvitationRepository
                .findById(id)
                .orElseThrow();
        var couple = coupleRepository.findById(invitation.getCouple_id())
                .orElseThrow();
        var member = memberRepository
                .findById(couple.getId())
                .orElseThrow();
        var ledger = ledgerRepository
                .findByCouple(couple)
                .orElse(null);

        return invitation.asDto(couple.getAccountNumber(),member.getNickname(),
                couple.getAccountBankName(),ledger == null ? null : ledger.getId());
    }

    public FormalInvitationDto updateFormalInvitation(long invitationId,ModifiedFormalInvitation invitation){
        return formalInvitationRepository
                .save(invitation.asEntity(invitationId, Util.getCoupleIdFromJwt(coupleRepository)))
                .asDto();
    }

    public void deleteFormalInvitationById(long id){
        formalInvitationRepository.deleteById(id);
    }

    public List<FormalInvitationDto> findAllFormalInvitation(){
        return formalInvitationRepository
                .findFormalInvitationByCoupleId(getCoupleIdFromJwt())
                .stream()
                .map(FormalInvitationEntity::asDto)
                .filter(it->it.getTitle() != null)
                .toList();
    }

    private FormalInvitationEntity getFormalIvnitationEntity(long id) {
        return formalInvitationRepository.findById(id).orElseThrow();
    }

    public FormalInvitationDto updateTitle(long id,String title) {
        FormalInvitationEntity invitation = getFormalIvnitationEntity(id);
        invitation.setTitle(title);
        return formalInvitationRepository.save(invitation).asDto();
    }

    private long getCoupleIdFromJwt()
    {
        return getCoupleIdFromMember(getMemberFromJwt());
    }

    private MemberDetails getMemberFromJwt() {
        return (MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private long getCoupleIdFromMember(MemberDetails member)
    {
        return coupleRepository
                .findByMember(member.getUser())
                .orElseThrow()
                .getId();
    }
}
