package com.on.server.domain.companyParticipant.application;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.application.FcmService;
import com.on.server.domain.alarm.domain.AlertType;
import com.on.server.domain.chat.domain.ChattingRoom;
import com.on.server.domain.chat.domain.SpecialChat;
import com.on.server.domain.chat.domain.repository.ChattingRoomRepository;
import com.on.server.domain.chat.domain.repository.SpecialChatRepository;
import com.on.server.domain.companyParticipant.domain.repository.CompanyParticipantRepository;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantRequestDTO;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantResponseDTO;
import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyParticipantService {

    private final CompanyParticipantRepository companyParticipantRepository;
    private final CompanyPostRepository companyPostRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final AlertService alertService;
    private final ChattingRoomRepository chattingRoomRepository;
    private final SpecialChatRepository specialChatRepository;

    public CompanyParticipantResponseDTO applyToCompanyPost(User user, CompanyParticipantRequestDTO requestDTO) {

        // 신청한 게시글 정보 가져오기
        CompanyPost companyPost = companyPostRepository.findById(requestDTO.getCompanyPostId())
                .orElseThrow(() -> new IllegalArgumentException("동행게시글을 찾을 수 없습니다. ID: "));

        // CompanyParticipant 엔티티 생성 및 저장 (상태 디폴트값은 AWAIT)
        CompanyParticipant companyParticipant = CompanyParticipant.builder()
                .user(user)
                .companyPost(companyPost)
                .companyParticipantstatus(CompanyParticipantStatus.AWAIT)
                .build();

        companyParticipantRepository.save(companyParticipant);

        String title = user.getNickname() + "님이 채팅을 시작했어요.";
        AlertType alertType = AlertType.COMPANY;
        String body = "다음 글에서 시작된 채팅이에요: " + companyPost.getTitle();

        alertService.sendAndSaveAlert(companyPost.getUser(), alertType, title, body, companyPost.getId());

        // 응답 DTO 생성 및 반환
        return CompanyParticipantResponseDTO.builder()
                .companyParticipantId(companyParticipant.getId())
                .companyPostId(companyParticipant.getCompanyPost().getId())
                .userId(companyParticipant.getUser().getId())
                .companyParticipantStatus(companyParticipant.getCompanyParticipantstatus())
                .build();
    }


    // 특정 채팅방의 동행 신청자의 동행 신청 상태 확인
    @Transactional(readOnly = true)
    public CompanyParticipantResponseDTO getCompanyParticipantStatus(Long userId, Long chattingRoomId) {

        // 채팅방이 존재하는지 확인
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "채팅방을 찾을 수 없습니다."));

        // userId로 User 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "사용자를 찾을 수 없습니다."));

        // SpecialChat 조회
        SpecialChat specialChat = specialChatRepository.findByChattingRoom(chattingRoom);

        // SpecialChat에서 CompanyPost 정보 가져오기
        CompanyPost companyPost = specialChat.getCompanyPost();

        // CompanyParticipant 조회 (status 확인용)
        CompanyParticipant companyParticipant = companyParticipantRepository.findByUser_IdAndCompanyPost_Id(userId, companyPost.getId());

        if (companyParticipant == null) { // 없으면 null 반환
            return null;
        }

        return CompanyParticipantResponseDTO.from(companyParticipant, chattingRoomId);
    }
}
