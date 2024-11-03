package com.akdong.we.common.auth;


import com.akdong.we.member.entity.Member;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.exception.member.MemberException;
import com.akdong.we.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberRepository.findByEmail(username)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_EMAIL_NOT_FOUND_ERROR));

		return new MemberDetails(member);
	}
}
