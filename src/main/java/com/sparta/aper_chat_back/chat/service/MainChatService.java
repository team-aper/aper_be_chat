package com.sparta.aper_chat_back.chat.service;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.entity.ChatParticipant;
import com.sparta.aper_chat_back.chat.entity.ChatRoom;
import com.sparta.aper_chat_back.chat.entity.ChatRoomView;
import com.sparta.aper_chat_back.chat.repository.ChatParticipantRepository;
import com.sparta.aper_chat_back.chat.repository.ChatRoomRepository;
import com.sparta.aper_chat_back.chat.repository.ChatRoomViewRepository;
import com.sparta.aper_chat_back.global.dto.ResponseDto;
import com.sparta.aper_chat_back.global.handler.exception.ServiceException;
import com.sparta.aper_chat_back.global.security.handler.ErrorCode;
import com.sparta.aper_chat_back.global.security.user.User;
import com.sparta.aper_chat_back.global.security.user.respository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomViewRepository viewRepository;

    public MainChatService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatRoomViewRepository viewRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.viewRepository = viewRepository;
    }

    @Transactional
    public ResponseDto<Void> createChat(Long userId, Long tutorId) {
        ChatRoom chatRoom = new ChatRoom();

        User user = findByIdAndCheckPresent(userId, false);
        User tutor = findByIdAndCheckPresent(tutorId, true);

        ChatParticipant userChatParticipant = new ChatParticipant(chatRoom, user, false);
        ChatParticipant tutorChatParticipant = new ChatParticipant(chatRoom, tutor, true);


        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(userChatParticipant);
        chatParticipantRepository.save(tutorChatParticipant);

        return ResponseDto.success("성공적으로 채팅방을 생성하였습니다.");
    }

    @Transactional
    public boolean isCreatedChat(Long userId, Long tutorId) {
        String tag = tutorId + "-" + userId;
        viewRepository.updateChatRoomParticipantsView();
        List<ChatRoomView> existingChatRoom = viewRepository.findByParticipants(tag);

        return !existingChatRoom.isEmpty();
    }

    @Transactional
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(Long userId) {
        List<ChatParticipant> participatingChats = chatParticipantRepository.findByUserUserId(userId);

        if (participatingChats.isEmpty()) {
            return ResponseDto.fail("참여 중인 채팅방이 없습니다");
        }

        List<ChatParticipatingResponseDto> participatingResponseDtos = new ArrayList<>();
        for (ChatParticipant chatParticipant : participatingChats) {
            ChatRoom chatRoom = chatParticipant.getChatRoom();
            if (!chatRoom.getIsRejected()) {
                ChatParticipatingResponseDto participatingResponseDto = new ChatParticipatingResponseDto(
                        chatRoom.getId(),
                        chatParticipant.getIsTutor(),
                        chatRoom.getIsAccepted(),
                        chatRoom.getStartTime()
                );
                participatingResponseDtos.add(participatingResponseDto);
            }
        }

        return ResponseDto.success("성공적으로 채팅방을 찾았습니다", participatingResponseDtos);
    }

    @Transactional
    public ResponseDto<Void> rejectChatRoomRequest(Long roomId, Long tutorId) {
        Optional<ChatParticipant> chatParticipantOptional = chatParticipantRepository.findByIsTutorAndUserUserIdAndChatRoomId(true, tutorId, roomId);

        if (chatParticipantOptional.isEmpty()) {
            return ResponseDto.fail("해당 채팅방 형성 요청이 없습니다.");
        }
        ChatRoom chatRoom = chatParticipantOptional.get().getChatRoom();

        if (chatRoom.getIsAccepted()) {
            return ResponseDto.fail("이미 요청을 수락하셨습니다.");
        }
        if (chatRoom.getIsRejected()) {
            return ResponseDto.fail("이미 요청을 거절하셨습니다.");
        }

        chatRoom.reject();
        chatRoomRepository.save(chatRoom);

        return ResponseDto.success("요청을 거절하였습니다.");
    }

    private User findByIdAndCheckPresent(Long id, Boolean tutor) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            if (tutor) {
                throw new ServiceException(ErrorCode.TUTOR_NOT_FOUND);
            }
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }
        return user.get();
    }


}
