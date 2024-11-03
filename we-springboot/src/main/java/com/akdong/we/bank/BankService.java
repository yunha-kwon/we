package com.akdong.we.bank;

import com.akdong.we.api.FinApiCallService;
import com.akdong.we.common.exception.BusinessException;
import com.akdong.we.couple.CoupleErrorCode;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.repository.CoupleRepository;
import com.akdong.we.couple.response.CoupleInfo;
import com.akdong.we.couple.service.CoupleService;
import com.akdong.we.ledger.LedgerService;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.entity.MemberAccount;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.repository.MemberAccountRepository;
import com.akdong.we.member.repository.MemberRepository;
import com.akdong.we.member.response.MemberInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankService {
    private final MemberAccountRepository memberAccountRepository;
    private final FinApiCallService finApiCallService;
    private final CoupleService coupleService;
    private final LedgerService ledgerService;
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<AccountInfo> accountList(Member member) throws JsonProcessingException {
        List<MemberAccount> myAccounts = memberAccountRepository.findByMember(member);
        List<String> accountNoList = new ArrayList<String>();


        for(MemberAccount memberAccount: myAccounts){
            accountNoList.add(memberAccount.getAccountNo());
        }
        JsonNode jsonResponse = finApiCallService.accountList(member.getUserKey());
        List<AccountInfo> response = new ArrayList<>();
        String coupleAccountNo = null;
        // 1. 커플 계좌 먼저 추가
        if(member.isCoupleJoined()){
            Couple couple = coupleService.getMyCoupleInfo(member);
            if(couple.getAccountNumber() != null){
                JsonNode coupleAccountInfo = finApiCallService.getCoupleAccount(member);
                AccountInfo coupleAccount = objectMapper.treeToValue(coupleAccountInfo, AccountInfo.class);
                coupleAccount.setAccountInfo("커플계좌");
                response.add(coupleAccount);
                coupleAccountNo = coupleAccount.getAccountNo();
            }
        }
        // 2. 대표 계좌, 일반 계좌 추가.
        for (JsonNode node : jsonResponse) {
            // node에 있는 accountNo가 accountNoList에 있다면 담는다.
            for(String accountNo : accountNoList){
                if(Objects.equals(accountNo, node.get("accountNo").asText())){
                    AccountInfo myAccount = objectMapper.treeToValue(node, AccountInfo.class);
                    if(Objects.equals(member.getPriorAccount(), myAccount.getAccountNo())){
                        myAccount.setAccountInfo("대표계좌");
                    }else{
                        if(Objects.equals(myAccount.getAccountNo(), coupleAccountNo)){
                            continue;
                        }
                        myAccount.setAccountInfo("일반계좌");
                    }
                    response.add(myAccount);
                }
            }
        }
        

        return response;
    }

    public MemberInfo registerPriorAccount(Member member, String accountNo){
        member.setPriorAccount(accountNo);
        memberRepository.save(member);
        return MemberInfo.of(member);
    }

    public CoupleInfo registerCoupleAccount(Member member, String accountNo, String bankName){
        Couple couple = coupleService.getMyCoupleInfo(member);

        couple.setAccountNumber(accountNo);
        couple.setAccountBankName(bankName);
        couple.setAccountOwnerId(member.getId());
        coupleRepository.save(couple);

        return CoupleInfo.of(couple);
    }

    public List<TransactionInfo> transactionHistory(Member member, String accountNo) throws JsonProcessingException {
        JsonNode jsonResponse = finApiCallService.getTransactionHistoryList(member.getUserKey(), accountNo);
        List<TransactionInfo> transactionInfoList = new ArrayList<>();

        for(JsonNode node : jsonResponse){
            transactionInfoList.add(objectMapper.treeToValue(node, TransactionInfo.class));
        }

        for(TransactionInfo transactionInfo: transactionInfoList){
            if(!transactionInfo.getTransactionAccountNo().isEmpty()){
                MemberAccount memberAccount = memberAccountRepository.findByAccountNo(transactionInfo.getTransactionAccountNo())
                        .orElseThrow(() -> new BusinessException(MemberErrorCode.ACCOUNT_MEMBER_ERROR));
                transactionInfo.setTransactionUserName(memberAccount.getMember().getNickname());
            }else{
                transactionInfo.setTransactionUserName("SYSTEM");
            }
        }

        return transactionInfoList;
    }
}
