package com.sparta.aper_chat_back.chat.service;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.dto.MessageDto;
import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import com.sparta.aper_chat_back.chat.entity.ChatParticipant;
import com.sparta.aper_chat_back.chat.entity.ChatRoom;
import com.sparta.aper_chat_back.chat.entity.ChatRoomView;
import com.sparta.aper_chat_back.chat.enums.ChatMessageEnum;
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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomViewRepository viewRepository;
    private final ChatService chatService;

    public MainChatService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatRoomViewRepository viewRepository, ChatService chatService) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.viewRepository = viewRepository;
        this.chatService = chatService;
    }

    @Transactional
    public Mono<ResponseDto<Void>> createChat(Long userId, Long tutorId, String message) {
        ChatRoom chatRoom = new ChatRoom();

        User user = findByIdAndCheckPresent(userId, false);
        User tutor = findByIdAndCheckPresent(tutorId, true);

        ChatParticipant userChatParticipant = new ChatParticipant(chatRoom, user, false);
        ChatParticipant tutorChatParticipant = new ChatParticipant(chatRoom, tutor, true);

        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(userChatParticipant);
        chatParticipantRepository.save(tutorChatParticipant);

        return sendSystemMessage(chatRoom.getId(), message, userId, user.getPenName())
                .thenReturn(ResponseDto.success(ChatMessageEnum.CREATE_CHAT_SUCCESS.getMessage()));
    }

    private Mono<Void> sendSystemMessage(Long chatRoomId, String message, Long userId, String userPenName) {
        MessageDto userRequestMessage = new MessageDto(chatRoomId, message, userId, 0L);
        Mono<ChatMessage> userMessageMono = chatService.saveMessage(userRequestMessage);

        String serviceMessage = String.format("1:1 수업 요청이 도착했어요. %s님과 1:1 수업을 진행할까요?", userPenName);
        MessageDto systemRequestMessage = new MessageDto(chatRoomId, serviceMessage, 0L, 1L);

        return userMessageMono
                .then(chatService.saveMessage(systemRequestMessage))
                .then();
    }

    @Transactional
    public Mono<Boolean> isCreatedChat(Long userId, Long tutorId) {
        return Mono.fromSupplier(() -> {
            String tag = tutorId + "-" + userId;
            viewRepository.updateChatRoomParticipantsView();
            List<ChatRoomView> existingChatRoom = viewRepository.findByParticipants(tag);
            return !existingChatRoom.isEmpty();
        });
    }

    @Transactional
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(Long userId) {
        List<ChatParticipant> participatingChats = chatParticipantRepository.findByUserUserId(userId);

        if (participatingChats.isEmpty()) {
            return ResponseDto.fail(ChatMessageEnum.NO_PARTICIPATING_CHAT.getMessage());
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

        return ResponseDto.success(ChatMessageEnum.FIND_CHAT_SUCCESS.getMessage(), participatingResponseDtos);
    }

    @Transactional
    public ResponseDto<Void> rejectChatRoomRequest(Long roomId, Long tutorId) {
        Optional<ChatParticipant> chatParticipantOptional = chatParticipantRepository.findByIsTutorAndUserUserIdAndChatRoomId(true, tutorId, roomId);

        if (chatParticipantOptional.isEmpty()) {
            return ResponseDto.fail(ChatMessageEnum.CHAT_REQUEST_MISSING.getMessage());
        }
        ChatRoom chatRoom = chatParticipantOptional.get().getChatRoom();

        if (chatRoom.getIsAccepted()) {
            return ResponseDto.fail(ChatMessageEnum.ALREADY_ACCEPTED.getMessage());
        }
        if (chatRoom.getIsRejected()) {
            return ResponseDto.fail(ChatMessageEnum.ALREADY_REJECTED.getMessage());
        }

        chatRoom.reject();
        chatRoomRepository.save(chatRoom);

        return ResponseDto.success(ChatMessageEnum.REQUEST_REJECTED.getMessage());
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
