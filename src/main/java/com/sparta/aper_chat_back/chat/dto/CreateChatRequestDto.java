package com.sparta.aper_chat_back.chat.dto;

import lombok.Getter;

@Getter
public class CreateChatRequestDto {
    private Long tutorId;
    private String message;
}
