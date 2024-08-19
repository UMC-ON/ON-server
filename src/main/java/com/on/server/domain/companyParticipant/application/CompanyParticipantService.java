package com.on.server.domain.companyParticipant.application;

import com.on.server.domain.companyParticipant.domain.repository.CompanyParticipantRepository;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantRequestDTO;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantResponseDTO;
import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
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

    public CompanyParticipantResponseDTO applyToCompanyPost(CompanyParticipantRequestDTO requestDTO) {

        // 신청자 유저 정보 가져오기
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: "));

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

        // 응답 DTO 생성 및 반환
        return CompanyParticipantResponseDTO.builder()
                .companyParticipantId(companyParticipant.getId())
                .companyPostId(companyParticipant.getCompanyPost().getId())
                .userId(companyParticipant.getUser().getId())
                .companyParticipantStatus(companyParticipant.getCompanyParticipantstatus())
                .build();
    }

    public CompanyParticipantResponseDTO updateStatus(Long participantId, CompanyParticipantStatus status) {

        // 특정 신청자 정보 가져오기
        CompanyParticipant companyParticipant = companyParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid participant ID"));

        // 상태 업데이트
        if (companyParticipant.getCompanyParticipantstatus() != CompanyParticipantStatus.AWAIT) {
            throw new RuntimeException("신청 상태가 AWAIT인 경우에만 PARTICIPANT로 업데이트할 수 있습니다.");
        }

        companyParticipant.setCompanyParticipantstatus(CompanyParticipantStatus.PARTICIPANT);
        companyParticipantRepository.save(companyParticipant);

        // 응답 DTO 생성 및 반환
        return CompanyParticipantResponseDTO.builder()
                .companyParticipantId(companyParticipant.getId())
                .companyPostId(companyParticipant.getCompanyPost().getId())
                .userId(companyParticipant.getUser().getId())
                .companyParticipantStatus(companyParticipant.getCompanyParticipantstatus())
                .build();
    }
}
