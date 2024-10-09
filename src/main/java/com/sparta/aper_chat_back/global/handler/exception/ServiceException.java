package com.sparta.aper_chat_back.global.handler.exception;

import com.sparta.aper_chat_back.global.security.dto.ErrorResponseDto;
import com.sparta.aper_chat_back.global.security.handler.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String code;
    private final String message;

    public ServiceException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponseDto toErrorResponse() {
        return new ErrorResponseDto(this.status.value(), this.code, this.message);
    }

}