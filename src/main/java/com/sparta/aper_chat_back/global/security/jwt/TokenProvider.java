package com.sparta.aper_chat_back.global.security.jwt;

import com.sparta.aper_chat_back.global.security.jwt.dto.GeneratedToken;
import com.sparta.aper_chat_back.global.security.jwt.service.RefreshTokenService;
import com.sparta.aper_chat_back.global.security.jwt.service.TokenValidationService;
import com.sparta.aper_chat_back.global.security.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;


@Slf4j
@Service
public class TokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L;  // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 3 * 24 * 60 * 60 * 1000L;  // 3 days
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final RefreshTokenService tokenService;
    private final Key accessKey;
    private final Key refreshKey;
    private final TokenValidationService tokenValidationService;

    public TokenProvider(RefreshTokenService tokenService, JwtProperties jwtProperties, TokenValidationService tokenValidationService) {
        this.tokenService = tokenService;
        this.accessKey = jwtProperties.accessKey();
        this.refreshKey = jwtProperties.refreshKey();
        this.tokenValidationService = tokenValidationService;
    }

    public GeneratedToken generateToken(String email, String role, String penName) {
        String accessToken = generateAccessToken(email, role, penName);
        String refreshToken = generateRefreshToken(email, role, penName);

        tokenService.saveTokenInfo(email, refreshToken, REFRESH_TOKEN_EXPIRATION);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateAccessToken(String email, String role, String penName) {
        return BEARER_PREFIX + generateToken(email, role, penName, ACCESS_TOKEN_EXPIRATION, accessKey);
    }

    public String generateRefreshToken(String email, String role, String penName) {
        return generateToken(email, role, penName, REFRESH_TOKEN_EXPIRATION, refreshKey);
    }

    private String generateToken(String email, String role, String penName, long expirationTime, Key key) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .claim("penName", penName)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public Claims getUserInfoFromAccessToken(String accessToken) {
        return tokenValidationService.verifyAccessToken(accessToken);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}
