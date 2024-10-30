package com.sparta.aper_chat_back.chat.dto;

import lombok.Getter;

@Getter
public class RejectChatRequestDto {
    Long chatRoomId;
    String message;
}
