package com.sparta.aper_chat_back.chat.dto;

import java.time.LocalDateTime;

public record SimplifiedChatParticipatingResponseDto(
        Long chatRoomId,
        Boolean isTutor,
        Boolean isAccepted,
        LocalDateTime startTime
) {
}
