package com.sparta.aper_chat_back.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private Boolean isRequested;

    private Long isAccepted;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    public ChatRoom(){
        this.startTime = LocalDateTime.now();
        this.isAccepted = -1L;
        this.isRequested = Boolean.FALSE;
    }

    public void reject() {
        this.isAccepted = -1L;
    }
}
