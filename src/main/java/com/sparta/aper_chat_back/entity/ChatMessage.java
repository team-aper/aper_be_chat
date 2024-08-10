package com.sparta.aper_chat_back.entity;

import com.sparta.aper_chat_back.dto.MessageRequestDto;
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
    private final Long senderId;
    private final Long chatRoomId;
    private final String content;
    private final LocalDateTime timestamp;

    public ChatMessage(MessageRequestDto requestDto) {
        this.senderId = requestDto.getSenderId();
        this.chatRoomId = requestDto.getChatRoomId();
        this.content = requestDto.getContent();
        this.timestamp = LocalDateTime.now();
    }
}
