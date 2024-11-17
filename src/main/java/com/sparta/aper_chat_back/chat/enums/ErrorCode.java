package com.sparta.aper_chat_back.chat.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST,"U001", "이미 가입된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"U002", "등록되지 않은 회원입니다."),
    EMAIL_SEND_FAILURE(HttpStatus.BAD_REQUEST,"U003", "인증코드 전송에 실패하였습니다."),
    EMAIL_AUTH_FAILED(HttpStatus.BAD_REQUEST,"U004", "이메일 인증에 실패하였습니다."),
    PASSWORD_CHANGE_ERROR(HttpStatus.BAD_REQUEST,"U005", "패스워드 변경에 실패하였습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST,"U006", "비밀번호가 일치하지 않습니다."),
    INVALID_REVIEW(HttpStatus.BAD_REQUEST, "UH007", "존재하지 않는 종류의 리뷰 입니다."),

    // Chat
    TUTOR_NOT_FOUND(HttpStatus.NOT_FOUND,"CH001", "존재하지 않는 튜터입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"CH002", "존재하지 않는 채팅방입니다."),
    NO_PARTICIPATING_CHAT(HttpStatus.NOT_FOUND, "CH003", "참여 중인 채팅방이 없습니다."),
    CHAT_ALREADY_PARTICIPATING(HttpStatus.BAD_REQUEST, "CH004", "이미 생성된 채팅방 입니다."),
    CHAT_ROOM_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "CH005", "해당 채팅방 형성 요청이 없습니다."),
    CHAT_ROOM_REQUEST_ACCEPTED(HttpStatus.BAD_REQUEST, "CH006", "이미 요청을 수락하셨습니다."),
    CHAT_ROOM_REQUEST_REJECTED(HttpStatus.BAD_REQUEST, "CH007", "이미 요청을 거절하셨습니다.");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String code, String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}