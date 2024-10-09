package com.sparta.aper_chat_back.chat.entity;

import com.sparta.aper_chat_back.chat.dto.MessageRequestDto;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="messages")
@Getter
public class ChatMessage {

    @Id
    private ObjectId id;
    private Long senderId;
    private Long chatRoomId;
    private String content;
    private Boolean read;
    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(MessageRequestDto requestDto) {
        this.senderId = requestDto.getSenderId();
        this.chatRoomId = requestDto.getChatRoomId();
        this.content = requestDto.getContent();
        this.timestamp = LocalDateTime.now();
        this.read = Boolean.FALSE;
    }
}
