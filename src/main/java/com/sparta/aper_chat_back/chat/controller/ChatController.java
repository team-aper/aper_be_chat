package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.docs.ChatControllerDocs;
import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import com.sparta.aper_chat_back.chat.service.ChatService;
import com.sparta.aper_chat_back.chat.docs.MainChatControllerDocs;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
public class ChatController implements ChatControllerDocs {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    @GetMapping("/history")
    public Flux<ChatMessage> getChatHistory(@RequestParam Long chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }

}
