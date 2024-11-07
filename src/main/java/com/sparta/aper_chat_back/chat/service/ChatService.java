package com.sparta.aper_chat_back.chat.service;

import com.sparta.aper_chat_back.chat.dto.MessageDto;
import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class ChatService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ReactiveMongoTemplate reactiveMongoTemplate, SimpMessagingTemplate messagingTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.messagingTemplate = messagingTemplate;
    }


    public Mono<ChatMessage> saveMessage(MessageDto messageDto) {
        String collectionName = "chat_" + messageDto.getChatRoomId();
        ChatMessage chatMessage = new ChatMessage(messageDto);
        return reactiveMongoTemplate.save(chatMessage, collectionName);
    }

    @Transactional
    public Flux<ChatMessage> getChatHistory(Long roomId) {
        String collectionName = "chat_" + roomId;
        return reactiveMongoTemplate.findAll(ChatMessage.class, collectionName);
    }

    @Transactional
    public Mono<ChatMessage> getLatestMessage(Long roomId) {
        String collectionName = "chat_" + roomId;
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);

        return reactiveMongoTemplate.findOne(query, ChatMessage.class, collectionName);
    }

//    @Transactional
//    public Mono<ChatMessage> saveAndBroadcastMessage(MessageRequestDto requestDto) {
//        return saveMessage(requestDto)
//                .doOnSuccess(savedMessage -> {
//                    messagingTemplate.convertAndSend("/subscribe/" + requestDto.getChatRoomId(), savedMessage);
//                });
//    }
}
