package com.sparta.aper_chat_back.chat.entity;

import com.sparta.aper_chat_back.global.security.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private Boolean isTutor;

    public ChatParticipant(){}

    public ChatParticipant(ChatRoom chatRoom, User user, Boolean isTutor) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.isTutor = isTutor;
    }
}
