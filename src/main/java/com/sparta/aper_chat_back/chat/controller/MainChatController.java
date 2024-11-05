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
        //Long userId = 4L;
        Long tutorId = createChatRequestDto.getTutorId();

        return mainChatService.createChat(userId, tutorId, createChatRequestDto.getMessage());
    }

    @GetMapping
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.getParticipatingChats(userDetails.user().getUserId());
        //return mainChatService.getParticipatingChats(4L);
    }

    @GetMapping("/{chatRoomId}")
    public Mono<ResponseDto<Void>> acceptChatRequest(@PathVariable Long chatRoomId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.acceptChatRequest(chatRoomId, userDetails.user().getUserId());
        //return mainChatService.acceptChatRequest(chatRoomId, 6L);
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
}