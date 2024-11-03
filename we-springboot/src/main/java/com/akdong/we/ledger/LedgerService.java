package com.akdong.we.ledger;

import com.akdong.we.common.exception.BusinessException;
import com.akdong.we.common.qr.QRCodeGenerator;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.repository.CoupleRepository;
import com.akdong.we.file.service.FileService;
import com.akdong.we.ledger.entity.Gift;
import com.akdong.we.ledger.entity.Ledger;
import com.akdong.we.ledger.entity.LedgerGift;
import com.akdong.we.ledger.repository.GiftRepository;
import com.akdong.we.ledger.repository.LedgerGiftRepository;
import com.akdong.we.ledger.repository.LedgerRepository;
import com.akdong.we.ledger.response.GiftInfo;
import com.akdong.we.member.entity.Member;
import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service("ledgerService")
@RequiredArgsConstructor
@Slf4j
public class LedgerService {
    private final LedgerRepository ledgerRepository;
    private final GiftRepository giftRepository;
    private final FileService fileService;
    private final LedgerGiftRepository ledgerGiftRepository;

    @Transactional
    public Ledger createLedger(Couple couple){
        // 이미 Ledger가 존재하는지 확인
        if (couple.isLedgerCreated()) {
            throw new BusinessException(LedgerErrorCode.COUPLE_ALREADY_CREATED_ERROR);
        }

        Ledger ledger = Ledger.builder()
                .couple(couple)
                .build();

        // Ledger 저장
        Ledger savedLedger = ledgerRepository.save(ledger);

        // QR 코드 생성 및 S3 업로드
        String ledgerAccessUrl = "we://transfer?id=" + savedLedger.getId();
        try {
            byte[] qrCodeBytes = QRCodeGenerator.generateQRCodeImage(ledgerAccessUrl, 350, 350);

            // 바이트 배열을 MultipartFile로 변환
            MultipartFile qrCodeMultipartFile = new MockMultipartFile(
                    "ledger_" + savedLedger.getId() + ".png", // 파일 이름
                    "ledger_" + savedLedger.getId() + ".png", // 원본 파일 이름
                    "image/png",                              // 파일 타입
                    qrCodeBytes                               // 파일 데이터
            );

            // S3에 업로드
            String qrCodeUrl = fileService.upload(qrCodeMultipartFile, "qr-codes");  // 디렉토리 이름 설정

            // QR 코드 URL을 저장
            log.info("QR code URL: {}", qrCodeUrl);
            savedLedger.setQrCodeUrl(qrCodeUrl);

        } catch (WriterException | IOException e) {
            throw new BusinessException(LedgerErrorCode.QR_GENERATE_FAILED_ERROR);
        }
        couple.setLedgerCreated(true);

        return savedLedger;
    }

    @Transactional
    public String findAccountByLedgerId(Long ledgerId){
        Ledger ledger = ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new BusinessException(LedgerErrorCode.LEDGER_NOT_FOUND_ERROR));

        Couple couple = ledger.getCouple();

        return couple.getAccountNumber();
    }

    @Transactional
    public List<GiftInfo> findLedgerGift(Long ledgerId, Boolean isBride){
        // Find all LedgerGift entries for the given ledgerId
        List<LedgerGift> ledgerGifts = ledgerGiftRepository.findByLedgerId(ledgerId);

        // Extract the Gift objects from the LedgerGift entries
        List<GiftInfo> giftInfoList = ledgerGifts.stream()
                .map(ledgerGift -> GiftInfo.of(ledgerGift.getGift()))
                .filter(giftInfo -> isBride == null || giftInfo.getIsBride() == isBride)
                .collect(Collectors.toList());

        return giftInfoList;
    }

    public List<Gift> findGift(Member member){
        return giftRepository.findByMemberAndMealTicketUrlIsNotNull(member);
    }
}
