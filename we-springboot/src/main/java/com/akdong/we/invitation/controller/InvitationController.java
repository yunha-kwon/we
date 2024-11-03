package com.akdong.we.invitation.controller;

import com.akdong.we.file.domain.FileDto;
import com.akdong.we.file.service.FileService;
import com.akdong.we.invitation.domain.FormalInvitationDto;
import com.akdong.we.invitation.domain.CustomInvitationDto;
import com.akdong.we.invitation.domain.formal.*;
import com.akdong.we.invitation.service.FormalInvitationService;
import com.akdong.we.invitation.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/invitation")
@Tag(name = "청첩장 API", description = "청첩장 관련 API")
@RequiredArgsConstructor
public class InvitationController {

    private final String DIR_NAME = "formal";

    private final InvitationService invitationService;
    private final FormalInvitationService formalInvitationService;
    private final FileService fileService;

    @MessageMapping("/send")
    public void send(CustomInvitationDto invitation) throws Exception {
        invitationService.handleInvitation(invitation);
    }

    @PostMapping("/formal")
    @Operation(summary = "빈 정보형 청첩장 생성", description = "빈 정보형 청첩장 생성")
    public EmptyFormalInvitation saveFormalInvitation() throws Exception {
        return formalInvitationService.saveFormalInvitation();
    }

    @PostMapping(value = "/formal/file/{invitationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "정보형 청첩장 파일 업로드", description = "정보형 청첩장 파일 업로드")
    public FileDto updateFormalInvitationFile(
            @PathVariable("invitationId") long id,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) throws IOException {
        formalInvitationService.updateTitle(id,title);
        return formalInvitationService.updateFormalInvitationFile(
                fileService.upload(file,DIR_NAME), id);
    }

    @PatchMapping("/formal/bride/{invitationId}")
    @Operation(summary = "정보형 청첩장 신부 정보 채우기", description = "정보형 청첩장 신부 정보 채우기")
    public PersonDto updateFormalInvitationBride(
            @PathVariable("invitationId") long id,
            @RequestBody PersonDto bride) {
        return formalInvitationService
                .updateFormalInvitationBride(id, bride);
    }

    @PatchMapping("/formal/groom/{invitationId}")
    @Operation(summary = "정보형 청첩장 신랑 정보 채우기", description = "정보형 청첩장 신랑 정보 채우기")
    public PersonDto updateFormalInvitationGroom(
            @PathVariable("invitationId") long id,
            @RequestBody PersonDto groom) {
        return formalInvitationService
                .updateFormalInvitationGroom(id, groom);
    }

    @PatchMapping("/formal/greetings/{invitationId}")
    @Operation(summary = "정보형 청첩장 인삿말 정보 채우기", description = "정보형 청첩장 인삿말 정보 채우기")
    public GreetingsDto updateFormalInvitationGreetings(
            @PathVariable("invitationId") long id,
            @RequestBody GreetingsDto greetings) {
        return formalInvitationService
                .updateFormalInvitationGreetings(id, greetings);
    }

    @PatchMapping("/formal/meta-info/{invitationId}")
    @Operation(summary = "정보형 청첩장 메타 정보 채우기", description = "정보형 청첩장 메타 정보 채우기")
    public MetaInfo updateFormalInvitationMetaInfo(
            @PathVariable("invitationId") long id,
            @RequestBody MetaInfo metaInfo) {
        return formalInvitationService.updateFormalInvitationMetaInfo(id,
                metaInfo);
    }

    @DeleteMapping("/formal/{invitationId}")
    @Operation(summary = "정보형 청첩장 삭제", description = "청첩장 ID로 삭제")
    public void deleteFormalInvitation(@PathVariable("invitationId") long id) {
        formalInvitationService.deleteFormalInvitationById(id);
    }

    @GetMapping("/formal/{invitationId}")
    @Operation(summary = "정보형 청첩장 조회", description = "청첩장 ID로 조회")
    public FormalInvitationDto findFormalInvitationById(@PathVariable("invitationId") Long invitationId) throws Exception {
        return formalInvitationService.findFormalInvitationById(invitationId);
    }

    @GetMapping("/formal/couple")
    @Operation(summary = "커플 ID로 모든 정보형 청첩장 조회", description = "커플 ID로 모든 청첩장 조회")
    public List<FormalInvitationDto> findAllFormalInvitation() throws Exception {
        return formalInvitationService.findAllFormalInvitation();
    }

    @PatchMapping("/formal/{invitationId}")
    @Operation(summary = "정보형 청첩장 전체 수정", description = "정보형 청첩장 전체 수정")
    public FormalInvitationDto updateFormalInvitation(
            @PathVariable("invitationId") long id,
            @RequestBody ModifiedFormalInvitation formalInvitation){
        return formalInvitationService.updateFormalInvitation(id, formalInvitation);
    }
}
