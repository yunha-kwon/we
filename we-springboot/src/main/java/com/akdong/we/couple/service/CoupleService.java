package com.akdong.we.couple.service;

import com.akdong.we.common.exception.BusinessException;
import com.akdong.we.couple.CoupleErrorCode;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.repository.CoupleRepository;
import com.akdong.we.couple.request.CoupleRegisterRequest;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service("coupleService")
@RequiredArgsConstructor
@Slf4j
public class CoupleService {
    private final CoupleRepository coupleRepository;
    private final MemberRepository memberRepository;

    private final RedisTemplate<String, String> redisTemplate;
    private static final long CODE_EXPIRATION = 5 * 60; // 5분 만료

    public String createCode(Long memberId) {
        String code = generateRandomCode();
        redisTemplate.opsForValue().set("couple:code:" + code, memberId.toString(), CODE_EXPIRATION, TimeUnit.SECONDS);
        return code;
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(1000000)); // 6자리 랜덤 숫자
    }

    public String getMemberIdByCode(String code) {
        return redisTemplate.opsForValue().get("couple:code:" + code);
    }

    public Couple createCouple(CoupleRegisterRequest request, Long memberId){
        String code = request.getCode();
        Long member1Id =Long.parseLong(getMemberIdByCode(code));
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR));

        Member member2 = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR));

        // 멤버가 이미 다른 커플에 속해 있는지 확인
        if (member1.isCoupleJoined() || member2.isCoupleJoined()) {
            throw new IllegalStateException("One or both members are already in a couple.");
        }

        // 새로운 Couple 객체 생성 및 초기화
        Couple couple = Couple.builder()
                .member1(member1)
                .member2(member2)
                .bankbookCreated(false)
                .weddingDate(request.getWeddingDate())
                .build();

        member1.setCoupleJoined(true);
        member2.setCoupleJoined(true);

        return coupleRepository.save(couple);
    }

    public Couple getMyCoupleInfo(Member member) {
        return coupleRepository.findByMember(member)
                .orElseThrow(() -> new BusinessException(CoupleErrorCode.COUPLE_NOT_FOUND_ERROR));
    }
}
