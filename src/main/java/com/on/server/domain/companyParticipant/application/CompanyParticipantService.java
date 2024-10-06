package com.on.server.domain.companyParticipant.application;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.application.FcmService;
import com.on.server.domain.alarm.domain.AlertType;
import com.on.server.domain.alarm.dto.FcmRequestDto;
import com.on.server.domain.companyParticipant.domain.repository.CompanyParticipantRepository;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantRequestDTO;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantResponseDTO;
import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyParticipantService {

    private final CompanyParticipantRepository companyParticipantRepository;
    private final CompanyPostRepository companyPostRepository;
    private final FcmService fcmService;
    private final AlertService alertService;

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
}
