package com.on.server.domain.chat.application;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.domain.AlertType;
import com.on.server.domain.chat.domain.Chat;
import com.on.server.domain.chat.domain.ChatType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.chat.domain.repository.ChatRepository;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.chat.dto.response.*;
import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.companyParticipant.domain.repository.CompanyParticipantRepository;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus.PARTICIPANT;

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

    private final AlertService alertService;

    public Page<CompanyChatRoomListDto> getCompanyChatRoomList(User user, Pageable pageable) {
        // '동행 구하기' 채팅방 목록
        Page<ChattingRoom> chattingRoomList = chattingRoomRepository.findByChatUserOneOrChatUserTwoAndChattingRoomType(user, ChatType.COMPANY, pageable);

        List<CompanyRoomDto> roomListDto = chattingRoomList.stream()
                .filter(chattingRoom -> chattingRoom.getChattingRoomType() == ChatType.COMPANY)
                .map(chattingRoom -> {

                    Chat chat = chatRepository.findFirstByChattingRoomOrderByCreatedAtDesc(chattingRoom);
                    SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
                    CompanyPost companyPost = specialChat.getCompanyPost();

                    User chatUserOne = chattingRoom.getChatUserOne();
                    User chatUserTwo = chattingRoom.getChatUserTwo();

                    String senderName = Objects.equals(user.getId(), chatUserOne.getId()) ? chatUserTwo.getNickname() : chatUserOne.getNickname();

                    String chatContents = (chat != null) ? chat.getContents() : null;
                    LocalDateTime lastChatTime = (chat != null) ? chat.getCreatedAt() : chattingRoom.getCreatedAt(); // 채팅 기록 없는 경우 채팅방 생성 시간

                    return new CompanyRoomDto(
                            chattingRoom.getId(),
                            senderName,
                            companyPost.getTravelArea().get(0),
                            chatContents,
                            lastChatTime
                    );
                })
                .toList();


        CompanyChatRoomListDto companyChatRoomListDto = CompanyChatRoomListDto.builder()
                .roomCount(roomListDto.size())
                .roomList(roomListDto)
                .build();

        return new PageImpl<>(List.of(companyChatRoomListDto), pageable, chattingRoomList.getTotalElements());
    }


    public Page<MarketChatRoomListDto> getMarketChatRoomList(User user, Pageable pageable) {
        // '중고 거래' 채팅방 목록
        Page<ChattingRoom> chattingRoomList = chattingRoomRepository.findByChatUserOneOrChatUserTwoAndChattingRoomType(user, ChatType.MARKET, pageable);

        List<MarketRoomDto> roomListDto = chattingRoomList.stream()
                .filter(chattingRoom -> chattingRoom.getChattingRoomType() == ChatType.MARKET)
                .map(chattingRoom -> {

                    Chat chat = chatRepository.findFirstByChattingRoomOrderByCreatedAtDesc(chattingRoom);

                    SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
                    MarketPost marketPost = specialChat.getMarketPost();

                    User chatUserOne = chattingRoom.getChatUserOne();
                    User chatUserTwo = chattingRoom.getChatUserTwo();

                    String senderName = Objects.equals(user.getId(), chatUserOne.getId()) ? chatUserTwo.getNickname() : chatUserOne.getNickname();

                    String chatContents = (chat != null) ? chat.getContents() : null;
                    LocalDateTime lastChatTime = (chat != null) ? chat.getCreatedAt() : chattingRoom.getCreatedAt(); // 채팅 기록 없는 경우 채팅방 생성 시간

                    String fileUrl = (!marketPost.getImages().isEmpty())
                            ? marketPost.getImages().get(0).getFileUrl()
                            : null;

                    return new MarketRoomDto(
                            chattingRoom.getId(),
                            senderName,
                            fileUrl, // 상품 이미지
                            chatContents,
                            lastChatTime
                    );
                })
                .toList();


        MarketChatRoomListDto marketChatRoomListDto = MarketChatRoomListDto.builder()
                .roomCount(roomListDto.size())
                .roomList(roomListDto)
                .build();

        return new PageImpl<>(List.of(marketChatRoomListDto), pageable, chattingRoomList.getTotalElements());
    }

    /* chatUserOne, chatUserTwo, specialChat 비교하여 기존에 존재하는 채팅방인지 찾는 메소드 */
    public Optional<ChattingRoom> findMatchingChatRoom(User user, User chatUserTwo, Long postId, ChatType chatType) {

        List<ChattingRoom> existingRooms = chattingRoomRepository.findChattingRoomByChatUserOneAndChatUserTwoAndChattingRoomType(user, chatUserTwo, chatType);
        List<SpecialChat> existSpecialChats;

        if (chatType == ChatType.COMPANY) {
            CompanyPost companyPost = companyPostRepository.findById(postId).orElse(null);
            existSpecialChats = specialChatRepository.findByCompanyPost(companyPost);
        } else {
            MarketPost marketPost = marketPostRepository.findById(postId).orElse(null);
            existSpecialChats = specialChatRepository.findByMarketPost(marketPost);
        }

        return existingRooms.stream()
                .filter(room -> existSpecialChats.stream()
                        .anyMatch(specialChat -> specialChat.getChattingRoom().getId().equals(room.getId()))
                )
                .findFirst();
    }

    @Transactional
    public ChatDto createChatRoom(User user, com.on.server.domain.chat.dto.request.ChatDto chatDto) {
        User chatUserTwo = userRepository.findById(chatDto.getReceiverId())
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        Long responseRoomId = 0L;

        if (chatDto.getChatType() == ChatType.COMPANY) {

            CompanyPost companyPost = companyPostRepository.findById(chatDto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

            Optional<ChattingRoom> existChattingRoom = findMatchingChatRoom(user, chatUserTwo, chatDto.getPostId(), ChatType.COMPANY);

            if (existChattingRoom.isPresent()) {
                responseRoomId = existChattingRoom.get().getId();
            } else { // 새로운 동행 구하기 채팅인 경우

                ChattingRoom chattingRoom = ChattingRoom.builder()
                        .chattingRoomType(chatDto.getChatType())
                        .chatUserOne(user) // 글 보고 채팅 신청하는 사람
                        .chatUserTwo(chatUserTwo) // 글 주인
                        .build();

                ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

                SpecialChat specialChat = SpecialChat.builder()
                        .chattingRoom(savedChattingRoom)
                        .user(chatUserTwo)
                        .companyPost(companyPost)
                        .specialChatType(chatDto.getChatType())
                        .build();

                specialChatRepository.save(specialChat);
                responseRoomId = savedChattingRoom.getId();

            }
        } else { // 물품 거래 채팅

            MarketPost marketPost = marketPostRepository.findById(chatDto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

            Optional<ChattingRoom> existChattingRoom = findMatchingChatRoom(user, chatUserTwo, chatDto.getPostId(), ChatType.MARKET);

            if (existChattingRoom.isPresent()) {
                responseRoomId = existChattingRoom.get().getId();
            } else { // 새로운 물품 거래 채팅인 경우
                ChattingRoom chattingRoom = ChattingRoom.builder()
                        .chattingRoomType(chatDto.getChatType())
                        .chatUserOne(user) // 글 보고 채팅 신청하는 사람
                        .chatUserTwo(chatUserTwo) // 글 주인
                        .build();

                ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);

                SpecialChat specialChat = SpecialChat.builder()
                        .chattingRoom(savedChattingRoom)
                        .user(chatUserTwo)
                        .marketPost(marketPost)
                        .specialChatType(chatDto.getChatType())
                        .build();

                specialChatRepository.save(specialChat);
                responseRoomId = savedChattingRoom.getId();
            }
        }

        return ChatDto.builder()
                .roomId(responseRoomId)
                .build();
    }

    /* 동행 채팅방 상단 정보 */
    public CompanyChatDto getCompanyInfo(User user, Long roomId) {
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

        CompanyChatDto companyChatDto = CompanyChatDto.builder()
                .isFullyRecruited(companyPost.getCurrentRecruitNumber() >= companyPost.getTotalRecruitNumber())
                .periodDay(companyPost.getSchedulePeriodDay())
                .startDate(companyPost.getStartDate())
                .endDate(companyPost.getEndDate())
                .location(companyPost.getTravelArea())
                .recruitNumber(companyPost.getTotalRecruitNumber())
                .participantNumber(companyPost.getCurrentRecruitNumber())
                .build();

        return companyChatDto;
    }

    /* 중고거래 채팅방 상단 정보 */
    public MarketChatDto getMarketInfo(User user, Long roomId) {
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

        String fileUrl = (!marketPost.getImages().isEmpty())
                ? marketPost.getImages().get(0).getFileUrl()
                : null;

        MarketChatDto marketChatDto = MarketChatDto.builder()
                .MarketPostId(specialChat.getMarketPost().getId()) // 물품 거래 postId
                .productName(marketPost.getTitle())
                .productPrice(marketPost.getCost())
                .tradeMethod(marketPost.getDealType())
                .imageUrl(fileUrl) // 이미지 없는 경우 null 반환
                .dealStatus(marketPost.getDealStatus()) // 상품 거래 여부
                .build();

        return marketChatDto;
    }

    public Page<ChatListDto> getMessageList(User user, Long roomId, Pageable pageable) {
        // 채팅 목록 조회
        ChattingRoom currentChattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        Page<Chat> chatList = chatRepository.findAllByChattingRoom(currentChattingRoom, pageable);

        List<ChatMessageDto> chatListDto = chatList.stream()
                .map(chat -> new ChatMessageDto(
                        chat.getContents(),
                        chat.getUser().getId(),
                        chat.getCreatedAt()
                )).toList();

        ChatListDto chatListResponseDto = ChatListDto.builder()
                .chatUserOne(currentChattingRoom.getChatUserOne().getId())
                .chatUserTwo(currentChattingRoom.getChatUserTwo().getId())
                .chatList(chatListDto)
                .build();

        return new PageImpl<>(List.of(chatListResponseDto), pageable, chatList.getTotalElements());
    }

    @Transactional
    public void postMessage(User user, Long roomId, String message) {
        ChattingRoom currentChattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new InternalServerException(ResponseCode.INVALID_PARAMETER));

        Chat chat = Chat.builder()
                .chattingRoom(currentChattingRoom)
                .user(user)
                .contents(message)
                .build();

        chatRepository.save(chat);

        String title = user.getNickname();
        String body = message;

        AlertType alertType;
        if (currentChattingRoom.getChattingRoomType() == ChatType.MARKET) { // 마켓 채팅일 경우
            alertType = AlertType.MARKET_CHAT;
        } else { // 동행 채팅일 경우
            alertType = AlertType.COMPANY_CHAT;
        }

        User alertUser = user != currentChattingRoom.getChatUserOne() ? currentChattingRoom.getChatUserOne() : currentChattingRoom.getChatUserTwo();

        alertService.sendAndSaveAlert(alertUser, alertType, title, body, roomId);
    }


    @Transactional
    public void completeRecruit(User user, Long roomId) {

        // 1. companyPost currentRecruitNumber +1 하기
        // roomId로 chattingRoom 찾기
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // chattingRoom으로 specialChat 찾기
        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);

        // specialChat에서 companyPost 찾기
        CompanyPost companyPost = specialChat.getCompanyPost();

        // 찾은 companyPost currentRecruitNumber +1 하기
        Long updateCurrentNumber = companyPost.getCurrentRecruitNumber() + 1;
        companyPost.updateCurrentNumber(updateCurrentNumber);

        if (updateCurrentNumber == companyPost.getTotalRecruitNumber()) {
            companyPost.updateRecruitCompleted(true);
        }

        companyPostRepository.save(companyPost);


        // 2. companyParticipant status -> participant로 바꾸기
        User userParticipant = chattingRoom.getChatUserOne();

        CompanyParticipant companyParticipant = companyParticipantRepository.findByUserAndCompanyPostId(userParticipant, companyPost.getId());

        // 상태 업데이트
        companyParticipant.setCompanyParticipantstatus(PARTICIPANT);

        companyParticipantRepository.save(companyParticipant);

    }

}


