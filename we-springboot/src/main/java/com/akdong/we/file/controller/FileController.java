package com.akdong.we.file.controller;

import com.akdong.we.file.service.FileService;
import com.akdong.we.invitation.repository.FormalInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;



}
