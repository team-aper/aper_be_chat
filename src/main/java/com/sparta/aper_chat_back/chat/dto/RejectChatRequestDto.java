package com.sparta.aper_chat_back.chat.dto;

public record RejectChatRequestDto(
        Long chatRoomId,
        String message
) {
}
