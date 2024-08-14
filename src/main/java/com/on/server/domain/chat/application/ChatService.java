package com.on.server.domain.chat.application;

import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChatType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.chat.domain.repository.ChatRepository;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.chat.dto.*;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
    private final CompanyPostRepository companyPostRepository;
    private final MarketPostRepository marketPostRepository;

    private final ChatRepository chatRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SpecialChatRepository specialChatRepository;

    public CompanyChatRoomListResponseDto getCompanyChatRoomList(User user) throws BaseRuntimeException {
        // 채팅방 목록
        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findChattingRoomByChatUserOneOrChatUserTwo(user, user);

        List<CompanyChatRoomListResponseDto.roomListDto> roomListDto = chattingRoomList.stream()
                .map(chattingRoom -> {

                    Chat chat = chatRepository.findFirstByChattingRoomOrderByCreatedAtDesc(chattingRoom);

                    SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
                    CompanyPost companyPost = companyPostRepository.findById(specialChat.getCompanyPost().getId()).orElse(null);

                    User chatUserOne = chattingRoom.getChatUserOne();
                    User chatUserTwo = chattingRoom.getChatUserTwo();

                    String senderName = Objects.equals(user.getId(), chatUserOne.getId()) ? chatUserTwo.getNickname() : chatUserOne.getNickname();

                    return new CompanyChatRoomListResponseDto.roomListDto(
                            chattingRoom.getId(),
                            senderName,
                            companyPost.getArrivePlace(),
                            chat.getContents(),
                            formatLastChatTime(chat.getCreatedAt())
                    );
                }).toList();

        CompanyChatRoomListResponseDto companyChatRoomListResponseDto = CompanyChatRoomListResponseDto.builder()
                .roomCount(chattingRoomList.size())
                .roomList(roomListDto)
                .build();

        return companyChatRoomListResponseDto;
    }

    private String formatLastChatTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toDays() == 0) {
            return createdAt.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
        } else if (duration.toDays() == 1) {
            return "1일 전";
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("M/d", Locale.ENGLISH));
        }
    }
//    public MarketChatRoomListResponseDto getMarketChatRoomList(User user) throws BaseRuntimeException {
//
//    }

    @Transactional
    public ChatResponseDto createChatRoom(User user, ChatRequestDto chatRequestDto) throws BaseRuntimeException {
        User chatUserTwo = userRepository.findById(chatRequestDto.getReceiverId())
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .chattingRoomType(chatRequestDto.getChatType())
                .chatUserOne(user) // 글 보고 채팅 신청하는 사람
                .chatUserTwo(chatUserTwo) // 글 주인
                .build();


        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

        if (chatRequestDto.getChatType() == ChatType.COMPANY) {
            CompanyPost companyPost = companyPostRepository.findById(chatRequestDto.getPostId()).orElse(null);

            SpecialChat specialChat = SpecialChat.builder()
                    .chattingRoom(savedChattingRoom)
                    .user(chatUserTwo)
                    .companyPost(companyPost)
                    .specialChatType(chatRequestDto.getChatType())
                    .build();

            specialChatRepository.save(specialChat);

        } else {
            MarketPost marketPost = marketPostRepository.findById(chatRequestDto.getPostId()).orElse(null);

            SpecialChat specialChat = SpecialChat.builder()
                    .chattingRoom(savedChattingRoom)
                    .user(chatUserTwo)
                    .marketPost(marketPost)
                    .specialChatType(chatRequestDto.getChatType())
                    .build();

            specialChatRepository.save(specialChat);

        }

        return ChatResponseDto.builder()
                .roomId(savedChattingRoom.getId())
                .build();
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

