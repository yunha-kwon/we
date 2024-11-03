package com.akdong.we.bank;

import com.akdong.we.api.FinApiCallService;
import com.akdong.we.common.dto.ErrorResponse;
import com.akdong.we.common.dto.SuccessResponse;
import com.akdong.we.common.exception.BusinessException;
import com.akdong.we.common.qr.QRCodeGenerator;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.response.CoupleInfo;
import com.akdong.we.couple.service.CoupleService;
import com.akdong.we.file.service.FileService;
import com.akdong.we.ledger.LedgerErrorCode;
import com.akdong.we.ledger.entity.Gift;
import com.akdong.we.ledger.entity.Ledger;
import com.akdong.we.ledger.entity.LedgerGift;
import com.akdong.we.ledger.repository.GiftRepository;
import com.akdong.we.ledger.repository.LedgerGiftRepository;
import com.akdong.we.ledger.repository.LedgerRepository;
import com.akdong.we.member.Login;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.entity.MemberAccount;
import com.akdong.we.member.exception.member.MemberErrorCode;
import com.akdong.we.member.repository.MemberAccountRepository;
import com.akdong.we.member.repository.MemberRepository;
import com.akdong.we.member.response.MemberInfo;
import com.akdong.we.notification.service.FirebaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("v1/bank")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank API", description = "Bank API입니다.")
public class BankController {
    private final FinApiCallService finApiCallService;
    private final CoupleService coupleService;
    private final FirebaseService firebaseService;
    private final BankService bankService;
    private final FileService fileService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GiftRepository giftRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerGiftRepository ledgerGiftRepository;
    private final MemberAccountRepository memberAccountRepository;


    @GetMapping("/my-account-test")
    @Operation(summary = "나의 계좌 목록(1원 송금 등록용)", description = "나의 계좌 목록을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 목록 조회 성공", useReturnTypeSchema = true),
    })
    public ResponseEntity<?> myAccountTest(@Parameter(hidden = true)  @Login Member member){
        try {
            JsonNode jsonResponse = finApiCallService.accountList(member.getUserKey());
            List<AccountInfo> response = new ArrayList<>();
            for (JsonNode node : jsonResponse) {
                response.add(objectMapper.treeToValue(node, AccountInfo.class));
            }
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "나의 계좌 리스트 조회에 성공했습니다.",
                            response
                    )
            );
        }catch (Exception e) {
            log.error("Exception occurred while processing the account list response: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse<>("계좌 리스트 조회 중 오류가 발생했습니다.", e));
        }
    }

    @GetMapping("/my-account")
    @Operation(summary = "나의 계좌 목록(등록 완료된 계좌)", description = "나의 계좌 목록(등록 완료된 계좌)을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 목록 조회 성공", useReturnTypeSchema = true),
    })
    public ResponseEntity<?> myAccount(@Parameter(hidden = true)  @Login Member member){
        try {
            List<AccountInfo> myAccounts = bankService.accountList(member);
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "나의 계좌(등록 완료된 계좌) 리스트 조회에 성공했습니다.",
                            myAccounts
                    )
            );
        }catch (Exception e) {
            log.error("Exception occurred while processing the account list response: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse<>("나의 계좌(등록 완료된 계좌) 리스트 조회 중 오류가 발생했습니다.", e));
        }
    }

    @PostMapping("/accountAuth")
    @Operation(summary = "1원 송금 요청", description = "1원 송금을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1원 송금 요청 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> accountAuth(
            @Parameter(hidden = true)  @Login Member member,
            @RequestBody PostAccountAuthRequest request) throws IOException {
        String authCode = finApiCallService.openAccountAuth(member.getUserKey(), request.getAccountNo());

        try{
            firebaseService.sendMessageTo(member.getId(), "인증 코드입니다.", authCode);
        }catch (Exception e){
            log.info(e.getMessage());
        }

        // "authCode": authCode 형식으로 Map을 생성
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("authCode", authCode);

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "1원 송금 요청에 성공했습니다.",
                        responseMap
                )
        );
    }

    @PostMapping("/checkAuthCode")
    @Operation(summary = "1원 송금 검증", description = "1원 송금 검증을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1원 송금 검증 요청 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> checkAuthCode(
            @Parameter(hidden = true) @Login Member member,
            @RequestBody CheckAuthUserRequest request){
        String status = finApiCallService.checkAuthCode(member.getUserKey(), request.getAccountNo(), request.getAuthText(), request.getAuthCode());

        // "authCode": authCode 형식으로 Map을 생성
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("Status", status);

        if(status.equals("SUCCESS")){
            MemberAccount memberAccount = MemberAccount.builder()
                    .member(member)
                    .accountNo(request.getAccountNo())
                    .build();
            memberAccountRepository.save(memberAccount);
        }

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "1원 송금 검증 요청에 성공했습니다.",
                        responseMap
                )
        );
    }

    @PostMapping("/transfer")
    @Operation(summary = "계좌 이체", description = "금융망으로 계좌 이체를 요청합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 이체 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> transfer(
            @Parameter(hidden = true)  @Login Member member,
            @RequestBody TransferRequest request) throws IOException {

        String responseCode = finApiCallService.transfer(member, request);

        if(Objects.equals(responseCode, "H0000") && request.getLedgerId() != null){

            Gift gift = Gift.builder()
                    .member(member)
                    .isBride(request.getIsBride())
                    .charge(request.getTransactionBalance())
                    .build();
            giftRepository.save(gift);

            // 식권 QR코드 생성
            // QR 코드 생성 및 S3 업로드
            String mealTicketUrl = "we://mealTicket?giftId="+gift.getId();
            try {
                byte[] qrCodeBytes = QRCodeGenerator.generateQRCodeImage(mealTicketUrl, 350, 350);

                // 바이트 배열을 MultipartFile로 변환
                MultipartFile qrCodeMultipartFile = new MockMultipartFile(
                        "mealTicket_" + gift.getId() + ".png", // 파일 이름
                        "mealTicket_" + gift.getId() + ".png", // 원본 파일 이름
                        "image/png",                              // 파일 타입
                        qrCodeBytes                               // 파일 데이터
                );

                // S3에 업로드
                String qrCodeUrl = fileService.upload(qrCodeMultipartFile, "qr-codes");  // 디렉토리 이름 설정

                // QR 코드 URL을 저장
                log.info("QR code URL: {}", qrCodeUrl);
                gift.setMealTicketUrl(qrCodeUrl);

            } catch (WriterException | IOException e) {
                throw new BusinessException(LedgerErrorCode.QR_GENERATE_FAILED_ERROR);
            }

            Ledger ledger = ledgerRepository.findById(request.getLedgerId())
                    .orElseThrow(() -> new BusinessException(LedgerErrorCode.LEDGER_NOT_FOUND_ERROR));

            LedgerGift ledgerGift = LedgerGift.builder()
                    .ledger(ledger)
                    .gift(gift)
                    .build();

            ledgerGiftRepository.save(ledgerGift);

            Couple couple = ledger.getCouple();
            Member member1 = couple.getMember1();
            Member member2 = couple.getMember2();

            firebaseService.sendMessageTo(member1.getId(), "입금 알림", String.valueOf(member.getNickname()+"님이 " +request.getTransactionBalance())+"원을 송금했습니다.");
            firebaseService.sendMessageTo(member2.getId(), "입금 알림", String.valueOf(member.getNickname()+"님이 " +request.getTransactionBalance())+"원을 송금했습니다.");
        } else if (Objects.equals(responseCode, "H0000") && request.getLedgerId() == null){
            MemberAccount memberAccount = memberAccountRepository.findByAccountNo(request.getDepositAccountNo())
                    .orElseThrow(() -> new BusinessException(MemberErrorCode.ACCOUNT_MEMBER_ERROR));
            Long memberId = memberAccount.getMember().getId();
            firebaseService.sendMessageTo(memberId, "입금 알림", String.valueOf(member.getNickname()+"님이 " +request.getTransactionBalance())+"원을 송금했습니다.");
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("STATUS", "Success");

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "송금에 성공했습니다.",
                        responseMap
                )
        );
    }

    @GetMapping("/my-couple-account")
    @Operation(summary = "나의 커플 계좌 정보", description = "나의 커플 계좌 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커플 계좌 조회 성공", useReturnTypeSchema = true),
    })
    public ResponseEntity<?> myCoupleAccount(@Parameter(hidden = true)  @Login Member member){
        if(!member.isCoupleJoined()){
            throw new BusinessException(MemberErrorCode.COUPLE_NOT_FOUND_ERROR);
        }

        try {
            JsonNode jsonResponse = finApiCallService.getCoupleAccount(member);
            AccountInfo response = objectMapper.treeToValue(jsonResponse, AccountInfo.class);

            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "나의 커플 계좌 조회에 성공했습니다.",
                            response
                    )
            );
        }catch (Exception e) {
            log.error("Exception occurred while processing the account list response: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse<>("계좌 리스트 조회 중 오류가 발생했습니다.", e));
        }

    }

    @PostMapping("/register-prior-account")
    @Operation(summary = "대표 계좌 등록", description = "나의 대표 계좌를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대표 계좌 등록 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> registerPriorAccount(@Parameter(hidden = true)  @Login Member member,
                                                  @RequestBody PostAccountAuthRequest request){
        MemberInfo memberInfo = bankService.registerPriorAccount(member, request.getAccountNo());

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "대표 계좌 등록에 성공했습니다.",
                        memberInfo
                )
        );
    }

    @PostMapping("/register-couple-account")
    @Operation(summary = "커플 계좌 등록", description = "나의 커플 계좌를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "커플 계좌 등록 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> registerCoupleAccount(@Parameter(hidden = true)  @Login Member member,
                                                   @RequestBody RegisterCoupleAccountRequest request){
        CoupleInfo coupleInfo = bankService.registerCoupleAccount(member, request.getAccountNo(), request.getBankName());

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "커플 계좌 등록에 성공했습니다.",
                        coupleInfo
                )
        );
    }

    @PostMapping("/transaction-history-list")
    @Operation(summary = "계좌 거래 내역 조회(축의금 포함)", description = "축의금을 포함한 모든 계좌 거래 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "계좌 거래 내역 조회 성공", useReturnTypeSchema = true),
    })
    @Transactional
    public ResponseEntity<?> transactionHistory(@Parameter(hidden = true)  @Login Member member,
                                                @RequestBody PostAccountAuthRequest request) throws JsonProcessingException {
        List<TransactionInfo> transactionInfoList = bankService.transactionHistory(member, request.getAccountNo());

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "계좌 거래 내역 조회에 성공했습니다.",
                        transactionInfoList
                )
        );
    }
}