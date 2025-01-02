package com.on.server.domain.companyParticipant.dto;
import com.on.server.domain.companyParticipant.domain.CompanyParticipant;
import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyParticipantResponseDTO {

    // 동행 참가자 ID
    private Long companyParticipantId;

    // 동행 구하기 글 ID
    private Long companyPostId;

    // 작성자 ID
    private Long userId;

    // 상태
    private CompanyParticipantStatus companyParticipantStatus;

    public static CompanyParticipantResponseDTO from(CompanyParticipant companyParticipant) {
       return CompanyParticipantResponseDTO.builder()
               .companyParticipantId(companyParticipant.getId())
               .companyPostId(companyParticipant.getCompanyPost().getId())
               .userId(companyParticipant.getUser().getId())
               .companyParticipantStatus(companyParticipant.getCompanyParticipantstatus())
               .build();
    }
}
