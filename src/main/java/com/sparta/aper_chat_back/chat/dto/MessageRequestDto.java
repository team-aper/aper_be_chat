package com.sparta.aper_chat_back.chat.dto;

import lombok.Getter;

@Getter
public class MessageRequestDto {
    private Long senderId;
    private Long chatRoomId;
    private String content;
}
