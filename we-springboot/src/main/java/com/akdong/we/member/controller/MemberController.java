package com.akdong.we.member.controller;

import com.akdong.we.common.dto.ErrorResponse;
import com.akdong.we.common.dto.SuccessResponse;
import com.akdong.we.file.domain.FileDto;
import com.akdong.we.file.service.FileService;
import com.akdong.we.member.Login;
import com.akdong.we.member.entity.Member;
import com.akdong.we.member.request.MemberRegisterPostReq;
import com.akdong.we.member.request.UpdateMemberInfoRequest;
import com.akdong.we.member.response.MemberInfo;
import com.akdong.we.member.service.AuthService;
import com.akdong.we.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/members")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Member API", description = "Member API 입니다.")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final FileService fileService;

    private final String DIR_NAME = "profile";

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "Member를 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                            "email": "이메일은 필수 입력값입니다.",
                            "password": "비밀번호는 필수 입력값입니다.",
                            "nickname" : "닉네임은 필수 입력값입니다."
                        }
                    }
                    """)))
    })
    public ResponseEntity<?> register(
            @RequestBody @Valid MemberRegisterPostReq registerInfo) {
        log.info("registerInfo = {}", registerInfo.getNickname());

        memberService.isLeavedMemberInRegister(registerInfo.getEmail());

        Member member = memberService.createUser(registerInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessResponse<>("성공적으로 회원가입 되었습니다.", member.getId())
        );
    }

    @GetMapping()
    @Operation(summary = "Member 조회", description = "로그인된 Member를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회되었습니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "조회에 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "다시 로그인해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 유저입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<?> getLoginMemberInfo(@Parameter(hidden = true)  @Login Member member
    ) {
        MemberInfo response = MemberInfo.of(member);

        return ResponseEntity.ok(
                new SuccessResponse<>("조회되었습니다.", response)
        );
    }

    @GetMapping("/email/{memberEmail}")
    @Operation(summary = "Member 조회", description = "Member email로 Member를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회되었습니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "조회에 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "다시 로그인해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 유저입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<?> getMemberInfo(@PathVariable(name = "memberEmail") @Valid String email) {

        log.info("memberEMAIL = {}", email);
        Member member = memberService.getMemberByEmail(email);

        return ResponseEntity.ok(
                new SuccessResponse<>("조회되었습니다.", MemberInfo.of(member))
        );
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "Member 조회", description = "Member id로 Member를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회되었습니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "조회에 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "다시 로그인해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 유저입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<?> getMemberInfoById(@PathVariable (name = "memberId") Long id){

        log.info("member id = {}", id);

        Member member = memberService.getMemberById(id);

        return ResponseEntity.ok(
                new SuccessResponse<>("조회되었습니다.", MemberInfo.of(member))
        );
    }

    @PatchMapping()
    @Operation(summary = "Member 업데이트", description = "로그인 된 Member의 정보를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "변경되었습니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "변경에 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "다시 로그인해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 유저입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<?> updateMemberInfo(@RequestBody UpdateMemberInfoRequest updateMemberInfoRequest, @Parameter(hidden = true) @Login Member member){
        log.info("member = {}", member);

        Member updatedMember = memberService.updateMemberInfo(updateMemberInfoRequest, member.getEmail());

        log.info("controller updateMember = {}", updatedMember);
        return ResponseEntity.ok(
                new SuccessResponse<>("변경되었습니다.", null)
        );
    }

    @DeleteMapping()
    @Operation(summary = "Member 탈퇴(논리 삭제)", description = "로그인 된 Member를 탈퇴합니다.(논리 삭제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴 되었습니다.", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "탈퇴에 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 입력입니다.",
                        "errors": {
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "다시 로그인해주세요.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject("""
                    {
                        "message": "유효하지 않은 유저입니다.",
                        "errors": {
                        }
                    }
                    """))),
    })
    public ResponseEntity<?> deleteMember(@Parameter(hidden = true) @Login Member member) {
        log.info("login memberId = {}, memberEmail = {}", member.getId(), member.getEmail());

        Member deletedmember = memberService.deleteMember(member.getId());

        return ResponseEntity.ok(
                new SuccessResponse<>("탈퇴 되었습니다.", MemberInfo.of(deletedmember))
        );
    }



}
