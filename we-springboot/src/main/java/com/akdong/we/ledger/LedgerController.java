package com.akdong.we.ledger;

import com.akdong.we.common.dto.SuccessResponse;
import com.akdong.we.common.exception.BusinessException;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.service.CoupleService;
import com.akdong.we.ledger.entity.Gift;
import com.akdong.we.ledger.entity.Ledger;
import com.akdong.we.ledger.response.GiftInfo;
import com.akdong.we.ledger.response.LedgerInfo;
import com.akdong.we.member.Login;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.exception.member.MemberErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/ledgers")
@Slf4j
@RequiredArgsConstructor
@Tag(name="Ledger API", description = "장부 API입니다.")
public class LedgerController {
    private final LedgerService ledgerService;
    private final CoupleService coupleService;

    @PostMapping
    @Operation(summary = "장부 생성", description = "장부를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "장부 생성 성공", useReturnTypeSchema = true)
    })
    public ResponseEntity<?> join(@Parameter(hidden = true)  @Login Member member) {
        Couple couple = coupleService.getMyCoupleInfo(member);
        Ledger ledger = ledgerService.createLedger(couple);

        // 데이터 객체 생성
        Map<String, Long> response = new HashMap<>();
        response.put("LedgerId", ledger.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessResponse<>("성공적으로 장부를 생성했습니다.", response)
        );
    }

    @GetMapping("/{ledgerId}/transfer")
    @Operation(summary = "장부 id로 계좌번호 조회", description = "장부 id로 계좌번호 조회합니다.(QR 링크 접속 이후 요청 전송하기)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장부 조회 성공", useReturnTypeSchema = true)
    })
    public ResponseEntity<?> findAccountByLedgerId(
            @PathVariable Long ledgerId,
            @Parameter(hidden = true)  @Login Member member
            ) {
        String account = ledgerService.findAccountByLedgerId(ledgerId);

        // 데이터 객체 생성
        Map<String, String> response = new HashMap<>();
        response.put("accountNo", account);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessResponse<>("성공적으로 계좌번호를 조회했습니다.", response)
        );
    }

    @GetMapping
    @Operation(summary = "MY 장부 조회", description = "로그인 정보로 커플의 축의금 장부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장부 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "405", description = "커플만 해당 기능을 사용할 수 있습니다.")
    })
    public ResponseEntity<?> findLedgerByCoupleId(
            @Parameter(hidden = true)  @Login Member member
    ) {
        Couple couple = coupleService.getMyCoupleInfo(member);

        Ledger ledger = couple.getLedger();

        // 데이터 객체 생성
        Map<String, LedgerInfo> response = new HashMap<>();
        response.put("ledgerInfo", LedgerInfo.of(ledger));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessResponse<>("성공적으로 장부를 조회했습니다.", response)
        );
    }

    @GetMapping("/myGift")
    @Operation(summary = "MY 축의금 조회(신랑측 + 신부측)", description = "로그인 정보로 축의금 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MY 축의금 조회 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "405", description = "커플만 해당 기능을 사용할 수 있습니다.")
    })
    public ResponseEntity<SuccessResponse<List<GiftInfo>>> findLedgerGift(
            @Parameter(hidden = true)  @Login Member member,
            @RequestParam(required = false) Boolean isBride
    ){
        Couple couple = coupleService.getMyCoupleInfo(member);
        Ledger ledger = couple.getLedger();
        List<GiftInfo> giftList = ledgerService.findLedgerGift(ledger.getId(), isBride);
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "MY 축의금 조회에 성공했습니다.",
                        giftList
                )
        );
    }

    @GetMapping("/myMealTicket")
    @Operation(summary = "MY 식권 리스트 조회", description = "로그인 정보로 나의 식권 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MY 축의금 조회 조회 성공", useReturnTypeSchema = true)
    })
    public ResponseEntity<SuccessResponse<List<String>>> findMealTicket(
            @Parameter(hidden = true)  @Login Member member
    ){
        List<Gift> giftList = ledgerService.findGift(member);
        List<String> mealTicketList = new ArrayList<>();
        for(Gift gift : giftList){
            mealTicketList.add(gift.getMealTicketUrl());
        }
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "MY 식권 리스트 조회에 성공했습니다.",
                        mealTicketList
                )
        );
    }
}
