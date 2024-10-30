package com.sparta.aper_chat_back.chat.enums;

public enum UserEnum {
    USER_NOT_FOUND("유저를 찾을 수 없습니다.");

    private final String message;

    UserEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
