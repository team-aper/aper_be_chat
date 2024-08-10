package com.sparta.aper_chat_back.controller;

import com.sparta.aper_chat_back.dto.MessageRequestDto;
import com.sparta.aper_chat_back.entity.ChatMessage;
import com.sparta.aper_chat_back.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Mono;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public Mono<Void> saveMessage(@Payload MessageRequestDto requestDto) {
        return chatService.saveMessage(requestDto);
    }
}
