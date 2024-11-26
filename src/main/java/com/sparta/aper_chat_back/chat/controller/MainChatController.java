package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.dto.CreateChatRequestDto;
import com.sparta.aper_chat_back.chat.dto.RejectChatRequestDto;
import com.sparta.aper_chat_back.chat.dto.SimplifiedChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.enums.ChatMessageEnum;
import com.sparta.aper_chat_back.chat.service.ChatService;
import com.sparta.aper_chat_back.chat.service.MainChatService;
import com.sparta.aper_chat_back.global.docs.ChatControllerDocs;
import com.sparta.aper_chat_back.global.dto.ResponseDto;

import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin(origins = "*")
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
        Long tutorId = createChatRequestDto.getTutorId();

        return mainChatService.createChat(userId, tutorId, createChatRequestDto.getMessage());
    }

    @GetMapping("/{chatRoomId}")
    public Mono<ResponseDto<Void>> acceptChatRequest(@PathVariable Long chatRoomId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.acceptChatRequest(chatRoomId, userDetails.user().getUserId());

    }

    @GetMapping("/status/chatRoom")
    public ResponseDto<List<ChatParticipatingResponseDto>> checkReadStatus(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.checkReadStatus(userDetails.user().getUserId());
        //return mainChatService.checkReadStatus(1L);
    }


    @DeleteMapping("/status")
    public Mono<ResponseDto<Void>> rejectChatRequest(
            @RequestBody RejectChatRequestDto rejectChatRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //return mainChatService.rejectChatRoomRequest(rejectChatRequestDto.getChatRoomId(), 6L, rejectChatRequestDto.getMessage());
        return mainChatService.rejectChatRoomRequest(rejectChatRequestDto.getChatRoomId(), userDetails.user().getUserId(), rejectChatRequestDto.getMessage());
    }

    @DeleteMapping("/wrap/{chatRoomId}")
    public Mono<ResponseDto<Void>> terminateChat (
            @PathVariable Long chatRoomId
    ) {
        return mainChatService.terminateChat(chatRoomId);
    }

    @GetMapping
    public ResponseDto<List<SimplifiedChatParticipatingResponseDto>> getSimplifiedParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SimplifiedChatParticipatingResponseDto> response = mainChatService.getSimplifiedParticipatingChats(userDetails.user().getUserId());
        return ResponseDto.success("성공적으로 채팅방을 찾았습니다", response);
    }

}