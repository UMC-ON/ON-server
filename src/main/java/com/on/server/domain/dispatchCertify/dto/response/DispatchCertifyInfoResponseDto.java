package com.on.server.domain.dispatchCertify.dto.response;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.user.domain.DispatchType;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import lombok.Builder;
import lombok.Getter;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DispatchCertifyInfoResponseDto {

    private Long id;
    private Long userId;
    private String userName;
    private DispatchType dispatchType;
    private String dispatchedUniversity;
    private String universityUrl;
    private String country;
    private List<String> uuidFileUrlList;
    private PermitStatus permitStatus;
    private LocalDateTime createdAt;

    public static DispatchCertifyInfoResponseDto of(DispatchCertify dispatchCertify) {
        return DispatchCertifyInfoResponseDto.builder()
                .id(dispatchCertify.getId())
                .userId(dispatchCertify.getUser().getId())
                .userName(dispatchCertify.getUser().getName())
                .dispatchType(dispatchCertify.getDispatchType())
                .dispatchedUniversity(dispatchCertify.getDispatchedUniversity())
                .universityUrl(dispatchCertify.getUniversityUrl())
                .country(dispatchCertify.getCountry())
                .uuidFileUrlList(dispatchCertify.getUuidFileList().stream()
                        .map(UuidFile::getFileUrl)
                        .toList())
                .permitStatus(dispatchCertify.getPermitStatus())
                .createdAt(dispatchCertify.getCreatedAt())
                .build();
    }
}
