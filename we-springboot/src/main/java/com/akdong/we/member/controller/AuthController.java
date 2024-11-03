package com.akdong.we.member.controller;

import com.akdong.we.common.dto.ErrorResponse;
import com.akdong.we.common.dto.SuccessResponse;
import com.akdong.we.common.jwt.JwtUtil;
import com.akdong.we.common.redis.RedisUtil;
import com.akdong.we.couple.entity.Couple;
import com.akdong.we.couple.response.CoupleInfo;
import com.akdong.we.couple.service.CoupleService;
import com.akdong.we.member.Login;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.request.*;
import com.akdong.we.member.response.MemberAndTokensResponse;
import com.akdong.we.member.response.MemberInfo;
import com.akdong.we.member.service.AuthService;
import com.akdong.we.member.service.EmailService;
import com.akdong.we.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@RestController
@RequestMapping("/v1/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Auth API 입니다.")
public class AuthController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final AuthService authService;
    private final CoupleService coupleService;


    @PostMapping("/login")
    @Operation(summary = "로그인", description = "Email과 Password를 통해 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                            "email": "이메일은 필수 입력값입니다.",
                            "password": "비밀번호는 6자에서 12자 숫자 + 영어 + 특수기호를 넣어주세요.(임시)"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "아이디 또는 비밀번호를 확인해주세요.",
                        "errors": { }
                    }
                    """)))
    })
    public ResponseEntity<SuccessResponse<?>> login(@RequestBody MemberLoginPostReq loginInfo, HttpServletResponse response) {
        // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
        Map<String, String> tokens = authService.login(loginInfo);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", tokens.get("accessToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("Strict")
                .domain(".dr-study.kro.kr")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(2 * 7 * 24 * 60 * 60)
                .sameSite("Strict")
                .domain(".dr-study.kro.kr")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());


        Member loginMember = memberService.getMemberByEmail(loginInfo.getEmail());
        authService.isLeavedMemberInLogin(loginMember.getEmail());
        MemberAndTokensResponse memberAndTokensResponse = MemberAndTokensResponse
                .builder()
                .memberInfo(MemberInfo.of(loginMember))
                .tokens(tokens)  // couple이 없을 경우 coupleInfo는 포함하지 않음
                .build();

        return ResponseEntity.ok(
                new SuccessResponse<>("로그인 되었습니다.", memberAndTokensResponse)
        );
    }



    //로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "AccessToken을 통해 로그아웃을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "잘못된 토큰입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<SuccessResponse<String>> logout(@Parameter (hidden = true) @Login Member loginMember, HttpServletResponse response){
        log.info("member Login = {}", loginMember.getEmail());

        authService.logout(loginMember.getEmail());

        ResponseCookie accessCookie = ResponseCookie.from("access_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .domain(".dr-study.kro.kr")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .domain(".dr-study.kro.kr")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(
                new SuccessResponse<>("로그아웃 되었습니다.", null)
        );
    }



    @PostMapping("/access-token")
    @Operation(summary = "AccessToken 재발급 ", description = "RefreshToken을 통해서 AccessToken을 재발급 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AccessToken 재발급 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "AccessToken 재발급 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 토큰입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<SuccessResponse<?>> getAccessToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest request, HttpServletResponse response){



        String email = refreshTokenRequest.getEmail();
//        String refreshToken = request.getHeader(jwtUtil.HEADER_STRING);

        Cookie[] cookies = request.getCookies();
        String refreshToken = "";

        for(Cookie cookie : cookies){
            if("refresh_token".equals(cookie.getName())){
                refreshToken = cookie.getValue();
            }
        }


//        log.info("refreshToken = {}", refreshToken);
        log.info("cookie refresh_token = {}", refreshToken);

        String accessToken = authService.getAccessToken(email, refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(2 * 60 * 60)
                .sameSite("Strict")
                .domain(".dr-study.kro.kr")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok(
                new SuccessResponse<>("", accessToken)
        );
    }



    //회원가입할때 인증번호 보내기
    @PostMapping("/email-code")
    @Operation(summary = "회원가입 인증번호 전송", description = "회원가입 시 인증번호를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 전송 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "이미 가입된 이메일입니다.",
                        "errors": { }
                    }
                    """))),
    })

    public ResponseEntity<?> sendSignupEmail(@RequestBody EmailSendRequest emailSendRequest){
        emailService.sendSignupEmail(emailSendRequest.getEmail());

        //인증번호 redis에 유효기간 두고 저장
        //일단은 5분으로 설정해둠
//        redisUtil.setValueWithTTL(emailSendRequest.getEmail() + "email", asdf, Duration.ofMillis(1000 * 60 * 5L));

//        return ResponseEntity.status(200).body("인증번호 전송 = " + asdf);
        return ResponseEntity.ok(
                new SuccessResponse<>("인증번호가 전송되었습니다.", null)
        );
    }


    //회원가입할때 보냈던 인증번호 체크
    @GetMapping("/email-code")
    @Operation(summary = "회원가입 인증번호 체크", description = "회원가입 시 전송한 인증코드와 일치하는지 검사합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증번호 일치", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "인증번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "인증번호가 일치하지 않습니다. 다시 입력해주세요",
                        "errors": { }
                    }
                    """))),
            @ApiResponse(responseCode = "410", description = "인증번호 만료", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "인증번호가 만료되었습니다. 다시 요청해주세요",
                        "errors": { }
                    }
                    """))),
    })
    public ResponseEntity<?> verifyAuthNumber(@RequestBody VerifyEmailNumberRequest verifyEmailNumberRequest){
        //레디스에서 꺼낸 값이랑 보낸 인증번호랑 같은지 체크

        return ResponseEntity.ok(
                new SuccessResponse<>("인증번호가 일치합니다.", null)
        );
    }


    //비밀번호 찾기 할때 인증번호 보내기
    @PostMapping("/find-password")
    @Operation(summary = "비밀번호 변경 링크 전송", description = "비밀번호 변경을 위한 링크를 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 링크 전송 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 링크 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "잘못된 링크입니다.",
                        "errors": { }
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "비밀번호 변경 링크 만료", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "링크가 만료되었습니다. 다시 요청해주세요",
                        "errors": { }
                    }
                    """))),
    })
    public ResponseEntity<?> sendResetPasswordEmail(@RequestBody EmailSendRequest emailSendRequest){
        emailService.sendFindPasswordEmail(emailSendRequest.getEmail());

//        redisUtil.setValueWithTTL(uuid, emailSendRequest.getEmail(), Duration.ofMillis(1000 * 60 * 60 * 24));

//        return ResponseEntity.status(200).body("비밀번호 리셋 링크 전송");
        return ResponseEntity.ok(
                new SuccessResponse<>("비밀번호 변경 링크 전송", null)
        );
    }


    @PatchMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호 변경 링크를 통해 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                            "password": "비밀번호는 6자에서 12자 숫자 + 영어 + 특수기호를 넣어주세요.(임시)"
                         }
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "비밀번호 변경 링크 만료", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "존재하지 않는 링크입니다. 다시 요청해주세요",
                        "errors": { }
                    }
                    """))),
    })
    public ResponseEntity<?> resetPasswordEmail(@RequestParam(name = "token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest){
        log.info("token = {}", token);
        log.info("email = {}",resetPasswordRequest.getEmail());

        emailService.resetPasswordEmail(token, resetPasswordRequest);

        return ResponseEntity.ok(
                new SuccessResponse<>("비밀번호 변경 성공", null)
        );
    }

    @PostMapping("/check-password")
    @Operation(summary = "비밀번호 확인", description = "로그인된 회원의 패스워드를 검증해줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 일치", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "비밀번호가 일치하지 않습니다.",
                        "errors": {}
                    }
                    """))),
    })
    public ResponseEntity<?> checkPassword(@RequestBody VerifyPasswordRequest verifyPasswordRequest, @Login Member member){
        log.info("request password = {}", verifyPasswordRequest);
        log.info("login Member = {}", member);

        authService.verifyLoginMemberPassword(verifyPasswordRequest, member);

        return ResponseEntity.ok(
                new SuccessResponse<>("비밀번호가 일치합니다.", null)
        );

    }

}
