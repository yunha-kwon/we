package com.akdong.we.util;

import com.akdong.we.common.auth.MemberDetails;
import com.akdong.we.couple.repository.CoupleRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class Util {

    private Util(){}

    public static long getCoupleIdFromJwt(CoupleRepository coupleRepository)
    {
        return getCoupleIdFromMember(getMemberFromJwt(),coupleRepository);
    }

    public static long getUserIdFromJwt(){
        return ((MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUser()
                .getId();
    }

    private static MemberDetails getMemberFromJwt() {
        return (MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private static long getCoupleIdFromMember(
            MemberDetails member,
            CoupleRepository coupleRepository) {
        return coupleRepository
                .findByMember(member.getUser())
                .orElseThrow()
                .getId();
    }
}
