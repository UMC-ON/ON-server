package com.on.server.domain.dispatchCertify.application;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.dispatchCertify.domain.repository.DispatchCertifyRepository;
import com.on.server.domain.dispatchCertify.dto.request.DispatchCertifyApplyRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchCertifyService {

    private final DispatchCertifyRepository dispatchCertifyRepository;
    private final UuidFileService uuidFileService;

    public void applyDispatchCertify(User user, DispatchCertifyApplyRequestDto dispatchCertifyApplyRequestDto, List<MultipartFile> fileList) {
        if (fileList.isEmpty()) throw new BadRequestException(ResponseCode.FILE_NEEDED, "인증 사진이 필요합니다.");
        List<UuidFile> uuidFileList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            uuidFileList.add(uuidFileService.saveFile(file, FilePath.DISPATCH_CERTIFY));
        }

        DispatchCertify dispatchCertify = DispatchCertify.builder()
                .user(user)
                .dispatchType(dispatchCertifyApplyRequestDto.getDispatchType())
                .dispatchedUniversity(dispatchCertifyApplyRequestDto.getDispatchedUniversity())
                .universityUrl(dispatchCertifyApplyRequestDto.getUniversityUrl())
                .country(dispatchCertifyApplyRequestDto.getCountry())
                .permitStatus(PermitStatus.AWAIT)
                .uuidFileList(uuidFileList)
                .build();
        dispatchCertifyRepository.save(dispatchCertify);
    }

}
