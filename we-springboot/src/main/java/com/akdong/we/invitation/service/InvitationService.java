package com.akdong.we.invitation.service;

import com.akdong.we.invitation.domain.CustomInvitationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class InvitationService {
    public Map<Long, Map<String,Map<String,String>>> invitationInfos = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public void handleInvitation(CustomInvitationDto req) {
        setInvitation(req);
        sendInvitation(req);
        saveInvitation(req);
    }

    private void setInvitation(CustomInvitationDto req) {
        invitationInfos.computeIfAbsent(req.getInvitationId(),
                k -> new ConcurrentHashMap<>());
        invitationInfos.put(req.getInvitationId(),req.getTree());
    }

    private void sendInvitation(CustomInvitationDto req) {
        messagingTemplate.convertAndSend(
                "/topic/" + req.getInvitationId(),
                invitationInfos.get(req.getInvitationId()));
    }

    private void saveInvitation(CustomInvitationDto req) {

    }
}
