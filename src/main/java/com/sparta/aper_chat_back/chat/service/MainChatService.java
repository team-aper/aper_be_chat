package com.sparta.aper_chat_back.chat.service;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.dto.MessageDto;
import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import com.sparta.aper_chat_back.chat.entity.ChatParticipant;
import com.sparta.aper_chat_back.chat.entity.ChatRoom;
import com.sparta.aper_chat_back.chat.entity.ChatRoomView;
import com.sparta.aper_chat_back.chat.enums.ChatMessageEnum;
import com.sparta.aper_chat_back.chat.enums.UserEnum;
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
        return isCreatedChat(userId, tutorId)
                .flatMap(isCreated -> {
                    ChatRoom chatRoom;

                    if (isCreated) {
                        // 기존 채팅방 사용
                        Long chatRoomId = createdChatRoomId(userId, tutorId);
                        chatRoom = chatRoomRepository.findById(chatRoomId)
                                .orElseThrow(() -> new RuntimeException(ChatMessageEnum.CHAT_NOT_FOUND.getMessage()));
                    } else {
                        // 새로운 채팅방 생성
                        chatRoom = new ChatRoom();
                        chatRoomRepository.save(chatRoom);
                    }

                    // 사용자 및 튜터 찾기
                    User user = findByIdAndCheckPresent(userId, false);
                    User tutor = findByIdAndCheckPresent(tutorId, true);

                    // 채팅 참여자 추가
                    ChatParticipant userChatParticipant = new ChatParticipant(chatRoom, user, false);
                    ChatParticipant tutorChatParticipant = new ChatParticipant(chatRoom, tutor, true);
                    chatParticipantRepository.save(userChatParticipant);
                    chatParticipantRepository.save(tutorChatParticipant);

                    // 시스템 메시지 전송
                    return sendRequestSystemMessage(chatRoom.getId(), message, userId, user.getPenName())
                            .thenReturn(ResponseDto.success(ChatMessageEnum.CREATE_CHAT_SUCCESS.getMessage()));
                });
    }

    private Mono<Void> sendRequestSystemMessage(Long chatRoomId, String message, Long userId, String userPenName) {
        MessageDto userRequestMessage = new MessageDto(chatRoomId, message, userId, 0L);
        Mono<ChatMessage> userMessageMono = chatService.saveMessage(userRequestMessage);

        String serviceMessage = String.format("1:1 수업 요청이 도착했어요. %s님과 1:1 수업을 진행할까요?", userPenName);
        MessageDto systemRequestMessage = new MessageDto(chatRoomId, serviceMessage, 0L, 1L);

        MessageDto requestedMessage = new MessageDto(chatRoomId, ChatMessageEnum.CHAT_REQUESTED.getMessage(), 0L, 2L);

        return userMessageMono
                .then(chatService.saveMessage(systemRequestMessage))
                .then(chatService.saveMessage(requestedMessage))
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
    public Long createdChatRoomId(Long userId, Long tutorId) {
        String tag = tutorId + "-" + userId;
        viewRepository.updateChatRoomParticipantsView();
        ChatRoomView participatingChat = (ChatRoomView) viewRepository.findByParticipants(tag);
        return participatingChat.getChatRoomId();
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
            if (chatRoom.getIsAccepted() == 1L) {
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
    public Mono<ResponseDto<Void>> rejectChatRoomRequest(Long roomId, Long tutorId, String message) {
        Optional<ChatParticipant> chatParticipantOptional = chatParticipantRepository.findByIsTutorAndUserUserIdAndChatRoomId(true, tutorId, roomId);

        if (chatParticipantOptional.isEmpty()) {
            return Mono.just(ResponseDto.fail(ChatMessageEnum.CHAT_REQUEST_MISSING.getMessage()));
        }
        ChatRoom chatRoom = chatParticipantOptional.get().getChatRoom();

        if (!chatRoom.getIsRequested()) {
            return Mono.just(ResponseDto.fail(ChatMessageEnum.REQUEST_NOT_FOUND.getMessage()));
        }
        if (chatRoom.getIsAccepted() == 1L) {
            return Mono.just(ResponseDto.fail(ChatMessageEnum.ALREADY_ACCEPTED.getMessage()));
        }
        if (chatRoom.getIsAccepted() == -1L) {
            return Mono.just(ResponseDto.fail(ChatMessageEnum.ALREADY_REJECTED.getMessage()));
        }

        chatRoom.reject();
        chatRoomRepository.save(chatRoom);
        
        return sendRejectSystemMessage(roomId, tutorId, message)
                .then(Mono.just(ResponseDto.success(ChatMessageEnum.REQUEST_REJECTED.getMessage())));

    }

    private Mono<Void> sendRejectSystemMessage(Long chatRoomId, Long tutorId, String message) {
        Optional<User> optionalTutor = userRepository.findById(tutorId);
        if (optionalTutor.isEmpty()) {
            return Mono.error(new RuntimeException(UserEnum.USER_NOT_FOUND.getMessage()));
        }
        User tutor = optionalTutor.get();

        String serviceMessage = String.format("%s 님이 아래와 같은 사유로 수업을 거절했어요.", tutor.getPenName());
        MessageDto systemRejectMessage = new MessageDto(chatRoomId, serviceMessage, 0L, 3L);
        Mono<ChatMessage> systemMessageMono = chatService.saveMessage(systemRejectMessage);

        MessageDto userRequestMessage = new MessageDto(chatRoomId, message, tutorId, 0L);
        Mono<ChatMessage> userMessageMono = chatService.saveMessage(userRequestMessage);

        return systemMessageMono
                .then(userMessageMono)
                .then();
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
