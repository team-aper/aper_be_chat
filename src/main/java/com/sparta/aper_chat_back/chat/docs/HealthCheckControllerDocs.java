package com.sparta.aper_chat_back.chat.docs;

import com.sparta.aper_chat_back.global.security.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "health-check-controller", description = "heartbeat와 healthcheck controller")
public interface HealthCheckControllerDocs {
    @Operation(summary = "서버 상태 확인", description = "서버의 상태를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서버 정상 작동",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/health")
    public String checkHealth();

    @Operation(summary = "인증 상태 확인", description = "현재 인증된 사용자의 정보를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증된 사용자 확인 성공",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/health/auth")
    public String authHealthCheck(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @Operation(summary = "채팅방 하트비트 전송", description = "지정된 채팅방에 하트비트를 전송하여 연결 상태를 유지합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "하트비트 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (ErrorCode: C002 - 유효성 검사 실패)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (ErrorCode: A001 - 인증에 실패하였습니다)"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ErrorCode: A009 - 사용자의 권한을 찾을 수 없습니다)"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음 (ErrorCode: CH001 - 존재하지 않는 채팅방입니다)"),
            @ApiResponse(responseCode = "500", description = "서버 오류 (ErrorCode: C001 - 내부 서버 오류가 발생했습니다)")
    })
    @PutMapping("/heartbeat/{chatRoomId}")
    public void heartbeat(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetailsImpl userDetails);
}
