package com.sparta.aper_chat_back.global.docs;

import com.sparta.aper_chat_back.chat.dto.ChatParticipatingResponseDto;
import com.sparta.aper_chat_back.chat.dto.CreateChatRequestDto;
import com.sparta.aper_chat_back.chat.dto.RejectChatRequestDto;
import com.sparta.aper_chat_back.chat.dto.SimplifiedChatParticipatingResponseDto;
import com.sparta.aper_chat_back.global.dto.ResponseDto;
import com.sparta.aper_chat_back.global.security.dto.ErrorResponseDto;
import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "chat-controller", description = "튜터와 채팅방 생성 및 관리 API")
public interface ChatControllerDocs {

    @Operation(summary = "채팅방 생성", description = "튜터와 새로운 채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "튜터를 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 튜터입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    Mono<ResponseDto<Void>> createChat(
            CreateChatRequestDto createChatRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "채팅 요청 거절", description = "특정 채팅방의 요청을 거절합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 거절 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "요청을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 요청입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    Mono<ResponseDto<Void>> rejectChatRequest(
            RejectChatRequestDto rejectChatRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "채팅 요청 수락", description = "특정 채팅방의 요청을 수락합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 수락 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "요청을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 요청입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    Mono<ResponseDto<Void>> acceptChatRequest(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "채팅방 읽음 상태 확인", description = "사용자가 참여 중인 채팅방의 읽음 상태를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽음 상태 확인 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<List<ChatParticipatingResponseDto>> checkReadStatus(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "채팅방 종료", description = "특정 채팅방을 종료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 종료 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 채팅방입니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    Mono<ResponseDto<Void>> terminateChat(@PathVariable Long chatRoomId);

    @Operation(summary = "단순 채팅방 목록 조회", description = "사용자가 참여 중인 간단한 정보의 채팅방 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단순 채팅방 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseDto<List<SimplifiedChatParticipatingResponseDto>> getSimplifiedParticipatingChats(@AuthenticationPrincipal UserDetailsImpl userDetails);
}
