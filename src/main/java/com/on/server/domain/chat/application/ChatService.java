package com.on.server.domain.chat.application;

import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChatType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.chat.domain.repository.ChatRepository;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.chat.dto.*;
import com.on.server.domain.companyParticipant.domain.repository.CompanyParticipantRepository;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.common.exceptions.UnauthorizedException;
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

    private final UserRepository userRepository;
    private final CompanyPostRepository companyPostRepository;
    private final MarketPostRepository marketPostRepository;
    private final CompanyParticipantRepository companyParticipantRepository;

    private final ChatRepository chatRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SpecialChatRepository specialChatRepository;

    public CompanyChatRoomListResponseDto getCompanyChatRoomList(User user) throws BaseRuntimeException {
        // '동행 구하기' 채팅방 목록
        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findChattingRoomByChatUserOneOrChatUserTwoAndChattingRoomType(user, user, ChatType.COMPANY);

        List<CompanyChatRoomListResponseDto.roomListDto> roomListDto = chattingRoomList.stream()
                .map(chattingRoom -> {

                    Chat chat = chatRepository.findFirstByChattingRoomOrderByCreatedAtDesc(chattingRoom);

                    SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
                    CompanyPost companyPost = companyPostRepository.findById(specialChat.getCompanyPost().getId())
                            .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

                    User chatUserOne = chattingRoom.getChatUserOne();
                    User chatUserTwo = chattingRoom.getChatUserTwo();

                    String senderName = Objects.equals(user.getId(), chatUserOne.getId()) ? chatUserTwo.getNickname() : chatUserOne.getNickname();

                    return new CompanyChatRoomListResponseDto.roomListDto(
                            chattingRoom.getId(),
                            senderName,
                            companyPost.getTravelArea().get(0),
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

    public MarketChatRoomListResponseDto getMarketChatRoomList(User user) throws BaseRuntimeException {
        // '중고 거래' 채팅방 목록
        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findChattingRoomByChatUserOneOrChatUserTwoAndChattingRoomType(user, user, ChatType.MARKET);

        List<MarketChatRoomListResponseDto.roomListDto> roomListDto = chattingRoomList.stream()
                .map(chattingRoom -> {

                    Chat chat = chatRepository.findFirstByChattingRoomOrderByCreatedAtDesc(chattingRoom);

                    SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
                    MarketPost marketPost = marketPostRepository.findById(specialChat.getMarketPost().getId())
                            .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

                    User chatUserOne = chattingRoom.getChatUserOne();
                    User chatUserTwo = chattingRoom.getChatUserTwo();

                    String senderName = Objects.equals(user.getId(), chatUserOne.getId()) ? chatUserTwo.getNickname() : chatUserOne.getNickname();

                    return new MarketChatRoomListResponseDto.roomListDto(
                            chattingRoom.getId(),
                            senderName,
                            marketPost.getImages().get(0).getFileUrl(), // 상품 이미지
                            chat.getContents(),
                            formatLastChatTime(chat.getCreatedAt())
                    );
                }).toList();

        MarketChatRoomListResponseDto marketChatRoomListResponseDto = MarketChatRoomListResponseDto.builder()
                .roomCount(chattingRoomList.size())
                .roomList(roomListDto)
                .build();

        return marketChatRoomListResponseDto;
    }

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


    public CompanyChatResponseDto getCompanyInfo(User user, Long roomId) throws BaseRuntimeException {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        // 현재 채팅방 유저인지 체크
        if (!Objects.equals(user.getId(), chattingRoom.getChatUserOne().getId())
                && !Objects.equals(user.getId(), chattingRoom.getChatUserTwo().getId())) {
            throw new UnauthorizedException(ResponseCode.API_NOT_ACCESSIBLE);
        }

        if (chattingRoom.getChattingRoomType() == ChatType.MARKET) {
            throw new BadRequestException(ResponseCode.INVALID_CHATTING_ROOM);
        }

        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
        CompanyPost companyPost = companyPostRepository.findById(specialChat.getCompanyPost().getId())
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        CompanyChatResponseDto companyChatResponseDto = CompanyChatResponseDto.builder()
                .isFullyRecruited(companyPost.getCurrentRecruitNumber() >= companyPost.getTotalRecruitNumber())
                .periodDay(companyPost.getSchedulePeriodDay())
                .startDate(companyPost.getStartDate())
                .endDate(companyPost.getEndDate())
                .location(companyPost.getTravelArea().get(0))
                .recruitNumber(companyPost.getTotalRecruitNumber())
                .participantNumber(companyPost.getCurrentRecruitNumber())
                .build();

        return companyChatResponseDto;
    }

    public MarketChatResponseDto getMarketInfo(User user, Long roomId) throws BaseRuntimeException {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        // 현재 채팅방 유저인지 체크
        if (!Objects.equals(user.getId(), chattingRoom.getChatUserOne().getId())
                && !Objects.equals(user.getId(), chattingRoom.getChatUserTwo().getId())) {
            throw new UnauthorizedException(ResponseCode.API_NOT_ACCESSIBLE);
        }

        if (chattingRoom.getChattingRoomType() == ChatType.COMPANY) {
            throw new BadRequestException(ResponseCode.INVALID_CHATTING_ROOM);
        }

        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
        MarketPost marketPost = marketPostRepository.findById(specialChat.getMarketPost().getId())
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));


        MarketChatResponseDto marketChatResponseDto = MarketChatResponseDto.builder()
                .productName(marketPost.getTitle())
                .productPrice(marketPost.getCost())
                .tradeMethod(marketPost.getDealType())
                .imageUrl(marketPost.getImages().get(0).getFileUrl())
                .build();

        return marketChatResponseDto;
    }

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


//    @Transactional
//    public void completeRecruit(User user, Long roomId) {
//
//        // 1. companyPost currentRecruitNumber +1 하기
//        // roomId로 chattingRoom 찾기
//        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
//
//        // chattingRoom으로 specialChat 찾기
//        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
//
//        // specialChat으로 companyPost 찾기
//        CompanyPost companyPost = companyPostRepository.findBySpecialChat(specialChat);
//
//        // 찾은 companyPost currentRecruitNumber +1 하기
//        Long updateCurrentNumber = companyPost.getCurrentRecruitNumber() + 1 ;
//        companyPost.updateCurrentNumber(updateCurrentNumber);
//
//        companyPostRepository.save(companyPost);
//
//
//        // 2. companyParticipant status -> participant로 바꾸기
//        User userParticipant = chattingRoom.getChatUserOne();
//
//        CompanyParticipant companyParticipant = companyParticipantRepository.findByUser(userParticipant);
//
//        // 상태 업데이트
//        companyParticipant.setCompanyParticipantstatus(PARTICIPANT);
//
//        companyParticipantRepository.save(companyParticipant);
//
//    }

}


