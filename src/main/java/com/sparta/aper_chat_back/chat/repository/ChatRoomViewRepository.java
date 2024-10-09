package com.sparta.aper_chat_back.chat.repository;


import com.sparta.aper_chat_back.chat.entity.ChatRoomView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomViewRepository extends JpaRepository<ChatRoomView, Long>, ChatRoomViewRepositoryCustom {
    List<ChatRoomView> findByParticipants(String participants);
}
