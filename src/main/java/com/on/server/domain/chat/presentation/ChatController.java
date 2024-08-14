package com.on.server.domain.chat.presentation;

import com.on.server.domain.chat.application.ChatService;
import com.on.server.domain.chat.dto.ChatRequestDto;
import com.on.server.domain.user.application.UserService;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "Chat API")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SecurityService securityService;

    // ** 추후 삭제 예정 **
    @GetMapping("/test")
    @Operation(summary = "[백엔드] API 응답 통일 참고용", description = "백엔드 API 응답 통일 참고용 API 입니다. 추후 삭제 예정입니다.")
    public CommonResponse<?> apiTest() {
        try {
            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getApiTest());
        } catch (BaseRuntimeException e) {
            return new CommonResponse<>(e.getResponseCode());
        }
    }
    // ******************

    // [GET] 동행 - 채팅방 목록 조회
//    @GetMapping("/company/list")
//    @Operation(summary = "[동행] 채팅방 목록 조회 API", description = "특정 유저의 '동행' 채팅방 리스트를 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getCompanyChatRoomList(@AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            User user = securityService.getUserByUserDetails(userDetails);
//
//            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getCompanyChatRoomList(user));
//        } catch (BaseRuntimeException e) {
//            return new CommonResponse<>(e.getResponseCode());
//        }
//    }

    // [GET] 거래 - 채팅방 목록 조회
//    @GetMapping("/market/list")
//    @Operation(summary = "[거래] 채팅방 목록 조회 API", description = "특정 유저의 '거래' 채팅방 리스트를 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getMarketChatRoomList(@AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            User user = securityService.getUserByUserDetails(userDetails);
//
//            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMarketChatRoomList(user));
//        } catch (BaseRuntimeException e) {
//            return new CommonResponse<>(e.getResponseCode());
//        }
//    }


    // [POST] 채팅 요청 (채팅방 생성)
    @PostMapping("/request")
    @Operation(summary = "채팅 요청 API", description = "채팅 요청 API로, 채팅 요청 시 새로운 채팅방을 생성합니다.")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public CommonResponse<?> postChatRoom(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ChatRequestDto chatRequestDto) {
        try {
            User user = securityService.getUserByUserDetails(userDetails);
            chatService.createChatRoom(user, chatRequestDto);

            return new CommonResponse<>(ResponseCode.SUCCESS);
        } catch (BaseRuntimeException e) {
            return new CommonResponse<>(e.getResponseCode());
        }
    }

    // [GET] 동행 - 채팅 상단 정보 조회
//    @GetMapping("/company/{roomId}")
//    @Operation(summary = "[동행] 채팅 상단 정보 조회 API", description = "동행 채팅에서 상단의 희망시기, 장소, 모집 인원을 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getCompanyChatInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("roomId") Long roomId) {
//        try {
//            User user = securityService.getUserByUserDetails(userDetails);
//
//            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getCompanyInfo(user, roomId));
//        } catch (BaseRuntimeException e) {
//            return new CommonResponse<>(e.getResponseCode());
//        }
//    }

    // [GET] 거래 - 채팅 상단 정보 조회
//    @GetMapping("/market/{roomId}")
//    @Operation(summary = "[거래] 채팅 상단 정보 조회 API", description = "거래 채팅에서 상단의 상품 정보를 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getMarketChatInfo(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("roomId") Long roomId) {
//        try {
//            User user = securityService.getUserByUserDetails(userDetails);
//
//            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMarketInfo(user, roomId));
//        } catch (BaseRuntimeException e) {
//            return new CommonResponse<>(e.getResponseCode());
//        }
//    }

    // [GET] 채팅 내용 조회
    @GetMapping("/{roomId}/message")
    @Operation(summary = "채팅 내용 조회 API", description = "유저의 채팅 내역을 조회하는 API 입니다.")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public CommonResponse<?> getChattingMessage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("roomId") Long roomId) {
        try {
            User user = securityService.getUserByUserDetails(userDetails);

            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getMessageList(user, roomId));
        } catch (BaseRuntimeException e) {
            return new CommonResponse<>(e.getResponseCode());
        }
    }

    // [POST] 메시지 전송
    @PostMapping("/{roomId}/send")
    @Operation(summary = "메시지 전송 API", description = "생성된 채팅방에 새로운 메시지를 전송하는 API 입니다.")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public CommonResponse<?> postMessage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("roomId") Long roomId, @RequestBody String message) {
        try {
            User user = securityService.getUserByUserDetails(userDetails);

            chatService.postMessage(user, roomId, message);
            return new CommonResponse<>(ResponseCode.SUCCESS);
        } catch (BaseRuntimeException e) {
            return new CommonResponse<>(e.getResponseCode());
        }
    }
}
