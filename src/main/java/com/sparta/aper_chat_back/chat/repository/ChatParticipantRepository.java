package com.sparta.aper_chat_back.chat.repository;


import com.sparta.aper_chat_back.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    List<ChatParticipant> findByUserUserId(Long userId);

    Optional<ChatParticipant> findByIsTutorAndUserUserIdAndChatRoomId(boolean b, Long tutorId, Long roomId);

    Optional<ChatParticipant> findByChatRoomIdAndUserUserId(Long chatRoomId, Long userId);
}
