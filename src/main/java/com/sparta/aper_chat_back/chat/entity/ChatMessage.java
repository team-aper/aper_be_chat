package com.sparta.aper_chat_back.chat.entity;

import com.sparta.aper_chat_back.chat.dto.MessageDto;
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
    private Long sysNum;
    private String content;
    private Boolean read;
    private LocalDateTime timestamp;


    public ChatMessage() {}

    public ChatMessage(MessageDto messageDto) {
        this.senderId = messageDto.id();
        this.chatRoomId = messageDto.chatRoomId();
        this.sysNum = messageDto.sysNum();
        this.content = messageDto.content();
        this.timestamp = messageDto.regDate();
    }
}
