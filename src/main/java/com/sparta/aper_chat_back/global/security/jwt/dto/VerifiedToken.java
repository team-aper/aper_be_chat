package com.sparta.aper_chat_back.global.security.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifiedToken {
    private TokenVerificationResult tokenVerificationResult;
    private Claims claims;

}