package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.dto.CreateChatRequestDto;
import com.sparta.aper_chat_back.chat.dto.RejectChatRequestDto;
import com.sparta.aper_chat_back.chat.enums.ChatMessageEnum;
import com.sparta.aper_chat_back.chat.service.ChatService;
import com.sparta.aper_chat_back.chat.service.MainChatService;
import com.sparta.aper_chat_back.global.docs.ChatControllerDocs;
import com.sparta.aper_chat_back.global.dto.ResponseDto;
import com.sparta.aper_chat_back.global.security.user.User;
import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class MainChatController implements ChatControllerDocs {

    private final MainChatService mainChatService;

    @PostMapping
    public Mono<ResponseDto<Void>> createChat(
            @RequestBody CreateChatRequestDto createChatRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        //Long userId = 1L;
        Long tutorId = createChatRequestDto.getTutorId();

        // 비동기적으로 이미 생성된 채팅방인지 확인
        return mainChatService.isCreatedChat(userId, tutorId)
                .flatMap(isCreated -> {
                    if (isCreated) {
                        return Mono.just(ResponseDto.fail(ChatMessageEnum.ALREADY_CREATED.getMessage()));
                    }
                    // 채팅방 생성 요청
                    return mainChatService.createChat(userId, tutorId, createChatRequestDto.getMessage());
                });
    }

    @GetMapping
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.getParticipatingChats(userDetails.user().getUserId());
    }

    @GetMapping("/{chatRoomId}")
    public Mono<ResponseDto<Void>> acceptChatRequest(@PathVariable Long chatRoomId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.acceptChatRequest(chatRoomId, userDetails.user().getUserId());
    }

    @DeleteMapping("/status")
    public Mono<ResponseDto<Void>> rejectChatRequest(
            @RequestBody RejectChatRequestDto rejectChatRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //return mainChatService.rejectChatRoomRequest(rejectChatRequestDto.getChatRoomId(), 5L, rejectChatRequestDto.getMessage());
        return mainChatService.rejectChatRoomRequest(rejectChatRequestDto.getChatRoomId(), userDetails.user().getUserId(), rejectChatRequestDto.getMessage());
    }

    @DeleteMapping("/wrap/{chatRoomId}")
    public Mono<ResponseDto<Void>> terminateChat (
            @PathVariable Long chatRoomId
    ) {
        return mainChatService.terminateChat(chatRoomId);
    }
}