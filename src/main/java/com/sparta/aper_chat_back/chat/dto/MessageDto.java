package com.sparta.aper_chat_back.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        @JsonProperty("chatRoomId") Long chatRoomId,
        @JsonProperty("senderId") Long memberId,
        @JsonProperty("content") String content,
        @JsonProperty("system") Long sysNum,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class) LocalDateTime regDate
) {
    @JsonCreator
    public MessageDto(
            @JsonProperty("chatRoomId") Long chatRoomId,
            @JsonProperty("senderId") Long memberId,
            @JsonProperty("content") String content,
            @JsonProperty("regDate") LocalDateTime regDate,
            @JsonProperty("system") Long sysNum) {
        this(1L, chatRoomId, memberId, content, sysNum, regDate);
    }

    public MessageDto(Long chatRoomId, String content, Long userId, Long systemId) {
        this(1L, chatRoomId, userId, content, systemId, LocalDateTime.now());
    }

    public MessageDto(Long chatRoomId, String content, Long userId, Long systemId, LocalDateTime localDateTime) {
        this(1L, chatRoomId, userId, content, systemId, localDateTime);
    }
}