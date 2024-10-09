package com.sparta.aper_chat_back.chat.controller;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.service.ChatService;
import com.sparta.aper_chat_back.chat.service.MainChatService;
import com.sparta.aper_chat_back.global.docs.ChatControllerDocs;
import com.sparta.aper_chat_back.global.dto.ResponseDto;
import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
public class MainChatController implements ChatControllerDocs {

    private final MainChatService mainChatService;

    @PostMapping("/{tutorId}")
    public ResponseDto<Void> createChat(
            @PathVariable Long tutorId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.user().getUserId();
        if (mainChatService.isCreatedChat(userId, tutorId)) {
            return ResponseDto.fail("이미 생성된 채팅방 입니다.");
        }
        return mainChatService.createChat(userId, tutorId);
    }

    @GetMapping
    public ResponseDto<List<ChatParticipatingResponseDto>> getParticipatingChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.getParticipatingChats(userDetails.user().getUserId());
    }

    @DeleteMapping("/{roomId}")
    public ResponseDto<Void> rejectChatRequest(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mainChatService.rejectChatRoomRequest(roomId, userDetails.user().getUserId());
    }
}