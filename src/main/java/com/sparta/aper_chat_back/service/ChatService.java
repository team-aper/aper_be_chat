package com.sparta.aper_chat_back.service;

import com.sparta.aper_chat_back.dto.MessageRequestDto;
import com.sparta.aper_chat_back.entity.ChatMessage;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Void> saveMessage(MessageRequestDto requestDto) {
        String collectionName = "chatRoom_" + requestDto.getChatRoomId();
        ChatMessage chatMessage = new ChatMessage(requestDto);

        return reactiveMongoTemplate.save(chatMessage, collectionName).then();
    }
}
