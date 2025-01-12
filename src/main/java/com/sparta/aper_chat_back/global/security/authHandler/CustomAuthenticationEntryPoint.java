package com.sparta.aper_chat_back.global.security.authHandler;

import com.sparta.aper_chat_back.chat.enums.ErrorCode;
import com.sparta.aper_chat_back.global.security.exception.CustomResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        CustomResponseUtil.fail(response, ErrorCode.UNAUTHORIZED_USER);
    }
}
