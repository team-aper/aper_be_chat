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

    @JsonProperty("content")
    private String content;

    @JsonProperty("system")
    private Long sysNum;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;


    @JsonCreator
    public MessageDto(
            @JsonProperty("chatRoomId") Long chatRoomId,
            @JsonProperty("senderId") Long memberId,
            @JsonProperty("content") String content,
            @JsonProperty("regDate") LocalDateTime regDate,
            @JsonProperty("system") Long sysNum) {
        this.id = 1L;
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.content = content;
        this.regDate = regDate;
        this.sysNum = sysNum;
    }

    public MessageDto(Long chatRoomId, String content, Long userId, Long systemId) {
        this.chatRoomId = chatRoomId;
        this.memberId = userId;
        this.sysNum = systemId;
        this.regDate = LocalDateTime.now();
        this.content = content;
    }



    public void setRegDate(LocalDateTime now) {
        this.regDate = LocalDateTime.now();
    }

    public void setMessage(String sysMsg) {
        this.content = sysMsg;
    }

}
