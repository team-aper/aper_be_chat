package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.docs.HealthCheckControllerDocs;
import com.sparta.aper_chat_back.chat.service.MainChatService;
import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthCheckController implements HealthCheckControllerDocs {
    private final MainChatService mainChatService;

    public HealthCheckController(MainChatService mainChatService) {
        this.mainChatService = mainChatService;
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/health/auth")
    public String authHealthCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) { return userDetails.getUsername(); }

    @PutMapping("/heartbeat/{chatRoomId}")
    public void heartbeat(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        mainChatService.heartbeat(chatRoomId, userDetails.user().getUserId());
    }
}
