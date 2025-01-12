package com.sparta.aper_chat_back.global.security.authHandler;

import com.sparta.aper_chat_back.global.handler.CustomResponseUtil;
import com.sparta.aper_chat_back.global.handler.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        CustomResponseUtil.fail(response, ErrorCode.AUTH_NOT_FOUND);
    }
}
