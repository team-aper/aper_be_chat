package com.sparta.aper_chat_back.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private Long id;
    @JsonProperty("chatRoomId")
    private Long chatRoomId;

    @JsonProperty("senderId")
    private Long memberId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("content")
    private String content;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;


    @JsonCreator
    public MessageDto(
            @JsonProperty("chatRoomId") Long chatRoomId,
            @JsonProperty("senderId") Long memberId,
            @JsonProperty("message") String message,
            @JsonProperty("regDate") LocalDateTime regDate) {
        this.id = 1L;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.message = message;
        this.regDate = regDate;
    }


    public void setRegDate(LocalDateTime now) {
        this.regDate = LocalDateTime.now();
    }

    public void setMessage(String sysMsg) {
        this.message = sysMsg;
    }

}
