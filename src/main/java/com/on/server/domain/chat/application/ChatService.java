package com.on.server.domain.chat.application;

import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.repository.ChatRepository;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.chat.dto.ChatListResponseDto;
import com.on.server.domain.chat.dto.ChatRequestDto;
import com.on.server.domain.chat.dto.CompanyChatRoomListResponseDto;
import com.on.server.domain.chat.dto.MarketChatRoomListResponseDto;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    // ** 추후 삭제 예정 **
    public String getApiTest() throws BaseRuntimeException {
        return "API 응답 통일 테스트입니다.";
    }
    // ******************


    private final UserRepository userRepository;

    private final ChatRepository chatRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SpecialChatRepository specialChatRepository;

    public CompanyChatRoomListResponseDto getCompanyChatRoomList(User user) throws BaseRuntimeException {
        // 채팅방 목록
        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findChattingRoomByChatUserOneOrChatUserTwo(user, user);

        List<CompanyChatRoomListResponseDto.roomListDto> roomListDto = chattingRoomList.stream()
                .map(chattingRoom -> new CompanyChatRoomListResponseDto.roomListDto(
                        chattingRoom.getId(),
                        user.getNickname(),
                        "hh",
                        "aa",
                        "aa"
                )).toList();

        CompanyChatRoomListResponseDto companyChatRoomListResponseDto = CompanyChatRoomListResponseDto.builder()
                .roomCount(chattingRoomList.size())
                .roomList(roomListDto)
                .build();

        return companyChatRoomListResponseDto;
    }

//    public MarketChatRoomListResponseDto getMarketChatRoomList(User user) throws BaseRuntimeException {
//
//    }

    @Transactional
    public void createChatRoom(User user, ChatRequestDto chatRequestDto) throws BaseRuntimeException {
        User chatUserTwo = userRepository.findById(chatRequestDto.getReceiverId())
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .chattingRoomType(chatRequestDto.getChatType())
                .chatUserOne(user)
                .chatUserTwo(chatUserTwo)
                .build();

        chattingRoomRepository.save(chattingRoom);
    }


//    public CompanyChatResponseDto getCompanyInfo(User user, Long roomId) throws BaseRuntimeException {
//        CompanyChatResponseDto companyChatResponseDto = CompanyChatResponseDto.builder()
//                .
//                .build();

//        return companyChatResponseDto;
//    }

//    public MarketChatResponseDto getMarketInfo(User user, Long roomId) throws BaseRuntimeException {
//        MarketChatResponseDto marketChatResponseDto = MarketChatResponseDto.builder()
//                .productName()
//                .build()
//    }

    public ChatListResponseDto getMessageList(User user, Long roomId) throws BaseRuntimeException {
        // 채팅 목록 조회
        ChattingRoom currentChattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        List<Chat> chatList = chatRepository.findAllByChattingRoom(currentChattingRoom);

        List<ChatListResponseDto.chatListDto> chatListDto = chatList.stream()
                .map(chat -> new ChatListResponseDto.chatListDto(
                        chat.getContents(),
                        chat.getUser().getId()
                )).toList();

        ChatListResponseDto chatListResponseDto = ChatListResponseDto.builder()
                .currentUserId(user.getId())
                .chatList(chatListDto)
                .build();

        return chatListResponseDto;
    }

    @Transactional
    public void postMessage(User user, Long roomId, String message) throws BaseRuntimeException {
        ChattingRoom currentChattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        Chat chat = Chat.builder()
                .chattingRoom(currentChattingRoom)
                .user(user)
                .contents(message)
                .build();

        chatRepository.save(chat);
    }


}
