package com.sparta.aper_chat_back.chat.docs;

import com.sparta.aper_chat_back.chat.entity.ChatMessage;
import com.sparta.aper_chat_back.global.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@Tag(name = "chat-controller", description = "튜터와 채팅방 생성 및 관리 API")
public interface ChatControllerDocs {
    @Operation(summary = "과거 채팅 불러오기", description = "chatRoomId에 해당하는 채팅방 기록을 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 기록 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = ChatMessage.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 채팅방입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    Flux<ChatMessage> getChatHistory(@RequestParam Long chatRoomId);
}
