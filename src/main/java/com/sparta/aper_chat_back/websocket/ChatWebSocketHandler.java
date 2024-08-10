//package com.sparta.aper_chat_back.websocket;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//@Component
//public class ChatWebSocketHandler implements WebSocketHandler {
//
//    @Autowired
//    private ReactiveMongoTemplate reactiveMongoTemplate;
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//}
