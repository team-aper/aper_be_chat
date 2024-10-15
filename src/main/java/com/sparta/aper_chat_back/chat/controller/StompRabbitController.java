package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.dto.MessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class StompRabbitController {

    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";


    public StompRabbitController(RabbitTemplate template) {
        this.template = template;
    }

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(MessageDto chat, @DestinationVariable Long chatRoomId) {
        String sysMsg = "입장하셨습니다.";
        chat.setMessage(sysMsg);
        chat.setRegDate(LocalDateTime.now());
        template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(MessageDto chat, @DestinationVariable Long chatRoomId) {
        chat.setRegDate(LocalDateTime.now());
        template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
    }

    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(MessageDto msg) {
        System.out.println("received : " + msg.getMessage());
    }
}
