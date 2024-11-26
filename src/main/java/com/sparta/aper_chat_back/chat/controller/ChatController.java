package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import com.sparta.aper_chat_back.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "APIs for Chat", description = "load history chats, send chats to subscribers")
public class ChatController {

    private final ChatService chatService;
    private final Sinks.Many<ChatMessage> sink;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/history")
    @Operation(summary = "과거 채팅 불러오기", description = "chatRoomId에 해당하는 채팅방 기록을 불러옴")
    public Flux<ChatMessage> getChatHistory(@RequestParam Long chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }

}
