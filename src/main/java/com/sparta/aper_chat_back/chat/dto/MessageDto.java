package com.sparta.aper_chat_back.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    @JsonProperty("chatRoomId")
    private Long chatRoomId;

    @JsonProperty("senderId")
    private Long memberId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("regDate")
    private String region;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;


    public MessageDto(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.regDate = LocalDateTime.now();
    }

    public void setRegDate(LocalDateTime now) {
        this.regDate = LocalDateTime.now();
    }

    public void setMessage(String sysMsg) {
        this.message = sysMsg;
    }

}
