package com.akdong.we.member.service;

import com.akdong.we.common.jwt.JwtUtil;
import com.akdong.we.common.redis.RedisUtil;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.exception.auth.AuthErrorCode;
import com.akdong.we.member.exception.auth.AuthException;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.exception.member.MemberException;
import com.akdong.we.member.request.MemberLoginPostReq;
import com.akdong.we.member.request.VerifyPasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public String getAccessToken(String email, String refreshToken){

        if(!jwtUtil.correctRefreshToken(email, refreshToken)){
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }

        return jwtUtil.getAccessToken(email);
    }

    @Transactional
    public Map<String, String> login(MemberLoginPostReq memberLoginPostReq){
        String email = memberLoginPostReq.getEmail();
        String password = memberLoginPostReq.getPassword();

        Member member = memberService.getMemberByEmail(email);

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new AuthException(AuthErrorCode.AUTH_INVALID_ID_PASSWORD);
        }

        String accessToken = jwtUtil.getAccessToken(email);
        String refreshToken = jwtUtil.getRefreshToken(email);

        redisUtil.setValueWithTTL(email, jwtUtil.TOKEN_PREFIX + refreshToken, Duration.ofMillis(2 * 7 * 24 * 60 * 60 * 1000));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public void logout(String email){
        redisTemplate.delete(email);
    }

    public void verifyLoginMemberPassword(VerifyPasswordRequest verifyPasswordRequest, Member member){
        String inputPassword = verifyPasswordRequest.getPassword();
        String password = member.getPassword();
        log.info("password = {}", password);

        if(!passwordEncoder.matches(inputPassword, password)){
            log.info("비밀번호 일치 안하는데?");
            throw new AuthException(AuthErrorCode.INVALID_PASSWORD_EXCEPTION);
        }

        log.info("비밀번호 일치합니다.");

    }

    public void isLeavedMemberInLogin(String email){
        Member member = memberService.getMemberByEmail(email);

        log.info("isLeavedMember = {}", member.isLeaved());
        if(member.isLeaved()){
            throw new MemberException(MemberErrorCode.MEMBER_LEAVED_ERROR);
        }

    }

}
