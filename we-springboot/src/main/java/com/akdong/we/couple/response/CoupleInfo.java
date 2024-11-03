package com.akdong.we.couple.response;

import com.akdong.we.couple.entity.Couple;
import com.akdong.we.member.response.MemberInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Getter
@Builder
public class CoupleInfo {
    @Schema(description = "아이디", example = "1")
    private final long id;

    @Schema(description = "멤버 1")
    private final MemberInfo member1;

    @Schema(description = "멤버 2")
    private final MemberInfo member2;

    @Schema(description = "계좌 등록 여부", example = "true")
    private boolean bankbookCreated;

    @Column(name="계좌 번호")
    private String accountNumber;

    @Column(name ="장부 생성 여부")
    private boolean ledgerCreated;

    @Column(name ="결혼식까지 남은 날짜")
    private long dDay;

    public static CoupleInfo of(Couple couple) {
        if(couple.getWeddingDate() != null){
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inputDate = LocalDate.parse(couple.getWeddingDate(), formatter);
            long daysBetween = Math.abs(ChronoUnit.DAYS.between(inputDate, today));
            return builder()
                    .id(couple.getId())
                    .member1(MemberInfo.of(couple.getMember1()))
                    .member2(MemberInfo.of(couple.getMember2()))
                    .bankbookCreated(couple.isBankbookCreated())
                    .accountNumber(couple.getAccountNumber())
                    .ledgerCreated(couple.isLedgerCreated())
                    .dDay(daysBetween)
                    .build();
        }else{
            return builder()
                    .id(couple.getId())
                    .member1(MemberInfo.of(couple.getMember1()))
                    .member2(MemberInfo.of(couple.getMember2()))
                    .bankbookCreated(couple.isBankbookCreated())
                    .accountNumber(couple.getAccountNumber())
                    .ledgerCreated(couple.isLedgerCreated())
                    .build();
        }
    }
}
