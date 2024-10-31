package com.sparta.aper_chat_back.chat.enums;

public enum ChatMessageEnum {
    CREATE_CHAT_SUCCESS("성공적으로 채팅방을 생성하였습니다."),
    NO_PARTICIPATING_CHAT("참여 중인 채팅방이 없습니다."),
    FIND_CHAT_SUCCESS("성공적으로 채팅방을 찾았습니다."),
    REQUEST_REJECTED("요청을 거절하였습니다."),
    ALREADY_ACCEPTED("이미 요청을 수락하셨습니다."),
    ALREADY_REJECTED("이미 요청을 거절하셨습니다."),
    ALREADY_CREATED("이미 생성된 채팅방 입니다."),
    CHAT_REQUEST_MISSING("해당 채팅방 형성 요청이 없습니다."),
    CHAT_NOT_FOUND("채팅방을 찾을 수 없습니다."),
    CHAT_REQUESTED("1:1 수업 요청을 보냈어요. 곧 답변이 올거에요. 조금만 기다려주세요."),
    REQUEST_NOT_FOUND("튜터 신청 요청을 찾을 수 없습니다."),
    CHAT_ACCEPTED("요청을 수락하셨습니다."),
    CHAT_MONITORED("모든 대화과정은 모니터링되고 있으며, 혹시나 생기는 문제나 수업과 관련된 문의는 고객센터에서 해결해 드려요.");

    private final String message;

    ChatMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
