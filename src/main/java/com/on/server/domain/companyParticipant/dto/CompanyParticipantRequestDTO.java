package com.on.server.domain.companyParticipant.dto;

import ch.qos.logback.core.status.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompanyParticipantRequestDTO {

    // 동행 구하기 글 ID
    private Long companyPostId;

    // 작성자 ID
    private Long userId;

    // 상태
    private Status status;
}
