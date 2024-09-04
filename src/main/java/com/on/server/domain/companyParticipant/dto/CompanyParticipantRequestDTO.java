package com.on.server.domain.companyParticipant.dto;

import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyParticipantRequestDTO {

    // 동행 구하기 글 ID
    private Long companyPostId;
}
