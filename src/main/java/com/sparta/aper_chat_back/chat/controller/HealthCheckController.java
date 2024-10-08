package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/health/auth")
    public String authHealthCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) { return userDetails.getUsername(); }
}
