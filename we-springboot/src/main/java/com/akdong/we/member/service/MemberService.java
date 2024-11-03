package com.akdong.we.member.service;


import com.akdong.we.api.FinApiCallService;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.entity.MemberAccount;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.exception.member.MemberException;
import com.akdong.we.member.repository.MemberAccountRepository;
import com.akdong.we.member.repository.MemberRepository;
import com.akdong.we.member.request.MemberRegisterPostReq;
import com.akdong.we.member.request.UpdateMemberInfoRequest;
import com.akdong.we.member.request.UpdatedMemberInfoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */

@Service("memberService")
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;
	private final MemberAccountRepository memberAccountRepository;

	private final PasswordEncoder passwordEncoder;
	private final FinApiCallService finApiCallService;

	@Transactional
	public Member createUser(MemberRegisterPostReq userRegisterInfo) {

		// 이메일이 이미 존재하는지 확인
		if(memberRepository.findByEmail(userRegisterInfo.getEmail()).isPresent()){
			throw new MemberException(MemberErrorCode.MEMBER_EMAIL_EXIST_ERROR);
		}

		String userKey = finApiCallService.apiRegister(userRegisterInfo.getEmail());
		if(userKey == null){
			throw new MemberException(MemberErrorCode.API_REGISTER_ERROR);
		}

		Member member = Member.builder()
				.email(userRegisterInfo.getEmail())
				.password(passwordEncoder.encode(userRegisterInfo.getPassword()))
				.nickname(userRegisterInfo.getNickname())
				.pin(userRegisterInfo.getPin())
				.deviceToken(userRegisterInfo.getDeviceToken())
				.regDate(LocalDateTime.now())
				.userKey(userKey)
				.build();
		List<String> accountTypeUniqueNoList = new ArrayList<>();
		accountTypeUniqueNoList.add("001-1-0914b8aac1e947"); // 한국은행
		accountTypeUniqueNoList.add("004-1-ee1de29dfc5e4a"); // 국민은행
		accountTypeUniqueNoList.add("011-1-c147927f5d2e4b"); // 농협은행
		accountTypeUniqueNoList.add("020-1-c6f609414f854c"); // 우리은행
		accountTypeUniqueNoList.add("032-1-b302b3d33f4442"); // 대구은행
		accountTypeUniqueNoList.add("088-1-8836096e21e447"); // 신한은행
		accountTypeUniqueNoList.add("090-1-a52dc1fe801f4a"); // 카카오뱅크
		Random random = new Random();

		for(int i=0; i<4; i++){
			int idx = random.nextInt(accountTypeUniqueNoList.size());
			String accountTypeUniqueNo = accountTypeUniqueNoList.get(idx);
			accountTypeUniqueNoList.remove(idx);
			String accountNo = finApiCallService.makeAccount(userKey,accountTypeUniqueNo);
			if(accountNo == null){
				throw new MemberException(MemberErrorCode.API_MAKE_ACCOUNT_ERROR);
			}

			String transactionUniqueNo = finApiCallService.deposit(accountNo, 50000000L, "초기 자본 금액 입금", userKey);
			if(transactionUniqueNo == null){
				throw new MemberException(MemberErrorCode.API_DEPOSIT_ERROR);
			}
			log.info("입금 거래 고유번호 : {}",transactionUniqueNo);

//			MemberAccount memberAccount = MemberAccount.builder()
//					.member(member)
//					.accountNo(accountNo)
//					.build();
//
//			memberAccountRepository.save(memberAccount);
		}

		return memberRepository.save(member);
	}

	public Member getMemberByEmail(String email) {
//		 디비에 유저 정보 조회 (userId 를 통한 조회).
//		return memberRepository.findByUserId(userId);

		return memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_EMAIL_NOT_FOUND_ERROR));
	}

	public Member getMemberById(Long id){

		return memberRepository.findById(id)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR));
	}

	@Transactional
	public Member updateMemberInfo(UpdateMemberInfoRequest updateMemberInfoRequest, String email){
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR));

		String encoderPassword = updateMemberInfoRequest.getPassword() == null ?
				member.getPassword() : passwordEncoder.encode(updateMemberInfoRequest.getPassword());


		UpdatedMemberInfoRequest updatedMemberInfoRequest = UpdatedMemberInfoRequest
				.builder()
				.nickname(updateMemberInfoRequest.getNickname())
				.password(encoderPassword)
				.build();

		log.info("before member = {}", member);
		member.updateMemberInfo(updatedMemberInfoRequest);
		log.info("after member = {}", member);

		return member;
	}

	@Transactional
	public Member deleteMember(Long memberId){
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND_ERROR));

		member.delete();

		return member;
	}

	public void isLeavedMemberInRegister(String email){
		Optional<Member> member = memberRepository.findByEmail(email);

		if(member.isPresent() &&  member.get().isLeaved()){
			throw new MemberException(MemberErrorCode.MEMBER_LEAVED_ERROR);
		} else if(member.isPresent()){
			throw new MemberException(MemberErrorCode.MEMBER_EMAIL_EXIST_ERROR);
		}
	}
}
