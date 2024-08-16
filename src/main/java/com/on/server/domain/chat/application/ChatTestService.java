package com.on.server.domain.chat.application;

import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatTestService {

    private final ChattingRoomRepository chattingRoomRepository;

    private final SpecialChatRepository specialChatRepository;

    private final CompanyPostRepository companyPostRepository;

//    private final CompanyParticipantRepository companyParticipantRepository;

//    @Transactional
//    public void completeRecruit(User user, Long roomId) {
//
//        // 1. companyPost currentRecruitNumber +1 하기
//        //roomId로 chattingRoom 찾기
//        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
//
//        //chattingRoom으로 specialChat 찾기
//        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);
//
//        //specialChat으로 companyPost 찾기
//        CompanyPost companyPost = companyPostRepository.findBySpecialChat(specialChat);
//
//        //찾은 companyPost currentRecruitNumber +1 하기
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
//        //상태 업데이트
//        companyParticipant.setCompanyParticipantstatus(PARTICIPANT);
//
//        companyParticipantRepository.save(companyParticipant);
//
//
//
//    }
}
