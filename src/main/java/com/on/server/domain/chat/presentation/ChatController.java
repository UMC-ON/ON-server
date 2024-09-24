package com.on.server.domain.chat.presentation;

import com.on.server.domain.chat.application.ChatService;
import com.on.server.domain.chat.dto.request.ChatDto;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "Chat API")
public class ChatController {

    private final ChatService chatService;
    private final SecurityService securityService;

    // [GET] 동행 - 채팅방 목록 조회
    @GetMapping("/company/list")
    @Operation(summary = "[동행] 채팅방 목록 조회 API", description = "특정 유저의 '동행' 채팅방 리스트를 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> getCompanyChatRoomList(
            @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {

        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.getCompanyChatRoomList(user, pageable));
    }

    // [GET] 거래 - 채팅방 목록 조회
    @GetMapping("/market/list")
    @Operation(summary = "[거래] 채팅방 목록 조회 API", description = "특정 유저의 '거래' 채팅방 리스트를 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> getMarketChatRoomList(
            @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.getMarketChatRoomList(user, pageable));
    }


    // [POST] 채팅 요청 (채팅방 생성)
    @PostMapping("/request")
    @Operation(summary = "채팅 요청 API", description = "채팅 요청 API로, 채팅 요청 시 새로운 채팅방을 생성합니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> postChatRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChatDto chatDto
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.createChatRoom(user, chatDto));
    }

    // [GET] 동행 - 채팅 상단 정보 조회
    @GetMapping("/company/{roomId}")
    @Operation(summary = "[동행] 채팅 상단 정보 조회 API", description = "동행 채팅에서 상단의 희망시기, 장소, 모집 인원을 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> getCompanyChatInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("roomId") Long roomId
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.getCompanyInfo(user, roomId));
    }

    // [GET] 거래 - 채팅 상단 정보 조회
    @GetMapping("/market/{roomId}")
    @Operation(summary = "[거래] 채팅 상단 정보 조회 API", description = "거래 채팅에서 상단의 상품 정보를 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> getMarketChatInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("roomId") Long roomId
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.getMarketInfo(user, roomId));
    }

    // [GET] 채팅 내용 조회
    @GetMapping("/{roomId}/message")
    @Operation(summary = "채팅 내용 조회 API", description = "유저의 채팅 내역을 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> getChattingMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("roomId") Long roomId,
            @ParameterObject Pageable pageable
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(chatService.getMessageList(user, roomId, pageable));
    }

    // [POST] 메시지 전송
    @PostMapping("/{roomId}/send")
    @Operation(summary = "메시지 전송 API", description = "생성된 채팅방에 새로운 메시지를 전송하는 API 입니다.")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> postMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("roomId") Long roomId,
            @RequestBody String message
    ) {
        User user = securityService.getUserByUserDetails(userDetails);
        chatService.postMessage(user, roomId, message);

        return ResponseEntity.ok().build();
    }

    // [POST] 모집 완료
    @PostMapping("/{roomId}/recruit")
    @Operation(summary = "모집 완료 시 버튼 눌렀을 때")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<?> recruit(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("roomId") Long roomId
    ) {
        User user = securityService.getUserByUserDetails(userDetails);
        chatService.completeRecruit(user, roomId);

        return ResponseEntity.ok().build();
    }
}
