package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.dto.MessageDto;
import com.sparta.aper_chat_back.chat.service.ChatService;
import com.sparta.aper_chat_back.chat.service.MainChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cglib.core.Local;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class StompRabbitController {

    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange"; // will be used when the other not joined
    private final static String CHAT_QUEUE_NAME = "chat.queue";
    private final ChatService chatService;


    public StompRabbitController(RabbitTemplate template, ChatService chatService) {
        this.template = template;
        this.chatService = chatService;
    }

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(MessageDto chat, @DestinationVariable Long chatRoomId) {
        String sysMsg = "입장하셨습니다.";
        MessageDto updatedChat = new MessageDto(
                chat.chatRoomId(),
                sysMsg,
                chat.memberId(),
                chat.sysNum(),
                LocalDateTime.now()
        );
        template.convertAndSend("amq.topic", "room." + chatRoomId, updatedChat);
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(MessageDto chat, @DestinationVariable Long chatRoomId) {
        MessageDto updatedChat = new MessageDto(
                chat.chatRoomId(),
                chat.content(),
                chat.memberId(),
                chat.sysNum(),
                LocalDateTime.now()
        );
        chatService.saveMessage(updatedChat)
                .doOnSuccess(savedMessage -> {
                    template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
                })
                .subscribe();
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(MessageDto msg) {
        System.out.println("received : " + msg.content());
    }
}
