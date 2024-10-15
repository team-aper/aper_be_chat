package com.sparta.aper_chat_back.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private Long id;
    private Long chatRoomId;
    private Long memberId;
    private String message;
    private String region;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;


    public MessageDto(Long chatRoomId) {
        this.id = 1L;
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
