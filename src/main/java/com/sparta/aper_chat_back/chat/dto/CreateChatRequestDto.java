package com.sparta.aper_chat_back.chat.dto;

public record CreateChatRequestDto(
        Long tutorId,
        String message
) {
}
