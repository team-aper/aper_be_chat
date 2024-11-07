package com.sparta.aper_chat_back.chat.dto;

import java.time.LocalDateTime;

public record ChatParticipatingResponseDto(
    Long chatRoomId,
    Boolean isTutor,
    Boolean Read,
    String messageContent,
    LocalDateTime messageTimestamp

) {
}
