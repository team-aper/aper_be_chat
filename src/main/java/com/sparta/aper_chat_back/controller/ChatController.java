package com.sparta.aper_chat_back.controller;

import com.sparta.aper_chat_back.dto.MessageRequestDto;
import com.sparta.aper_chat_back.entity.ChatMessage;
import com.sparta.aper_chat_back.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class ChatController {

    private final ChatService chatService;
    private final Sinks.Many<ChatMessage> sink;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/history")
    public Flux<ChatMessage> getChatHistory(@RequestParam Long chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }

    @MessageMapping("/chat")
    public Mono<Void> saveMessage(@Payload MessageRequestDto requestDto) {
        return chatService.saveAndBroadcastMessage(requestDto)
                .doOnSuccess(sink::tryEmitNext)
                .then();
    }
}
