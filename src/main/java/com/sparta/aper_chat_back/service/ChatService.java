package com.sparta.aper_chat_back.service;

import com.sparta.aper_chat_back.chat.dto.MessageRequestDto;
import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ReactiveMongoTemplate reactiveMongoTemplate, SimpMessagingTemplate messagingTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Mono<ChatMessage> saveMessage(MessageRequestDto requestDto) {
        String collectionName = "chat_" + requestDto.getChatRoomId();
        ChatMessage chatMessage = new ChatMessage(requestDto);
        return reactiveMongoTemplate.save(chatMessage, collectionName)
                .thenReturn(chatMessage);
    }

    @Transactional
    public Flux<ChatMessage> getChatHistory(Long roomId) {
        String collectionName = "chat_" + roomId;
        return reactiveMongoTemplate.findAll(ChatMessage.class, collectionName);
    }


    @Transactional
    public Mono<ChatMessage> saveAndBroadcastMessage(MessageRequestDto requestDto) {
        return saveMessage(requestDto)
                .doOnSuccess(savedMessage -> {
                    messagingTemplate.convertAndSend("/subscribe/" + requestDto.getChatRoomId(), savedMessage);
                });
    }
}
