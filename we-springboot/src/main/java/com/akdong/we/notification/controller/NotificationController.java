package com.akdong.we.notification.controller;

import com.akdong.we.notification.domain.TokenDto;
import com.akdong.we.notification.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "FCM 알림 API", description = "FCM 알림 관련 API")
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final FirebaseService firebaseService;


    @PostMapping("token")
    @Operation(summary = "토큰 등록하기", description = "토큰 등록하기")
    public TokenDto registToken(@RequestParam("token") String token) {
        return firebaseService.registToken(token);
    }

    @PatchMapping("token")
    @Operation(summary = "토큰 수정", description = "FCM 토큰 수정하기")
    public TokenDto updateToken(@RequestParam("token") String token) {
        return firebaseService.registToken(token);
    }

    @GetMapping("test-send")
    @Operation(summary = "FCM 알림 테스트 전송", description = "user_id를 담아서 보내시면 해당 유저 기기에 FCM 알림 전송합니다.")
    public String testFcmMsg(@RequestParam("user_id") long user_id) throws Exception {
        var response = firebaseService.sendMessageTo(user_id,"테스트용 제목","테스트 용 메세지 입니다.");
        if(response.code() >= 400) {
            return response.body().string();
        }
        return "알림 전송 성공";
    }
}
