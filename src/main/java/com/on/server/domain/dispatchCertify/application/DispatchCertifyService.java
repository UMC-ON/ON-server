package com.on.server.domain.dispatchCertify.application;

import com.on.server.domain.dispatchCertify.domain.DispatchCertify;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.dispatchCertify.domain.repository.DispatchCertifyRepository;
import com.on.server.domain.dispatchCertify.dto.request.DispatchCertifyApplyRequestDto;
import com.on.server.domain.dispatchCertify.dto.response.DispatchCertifyInfoResponseDto;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DispatchCertifyService {

    private final DispatchCertifyRepository dispatchCertifyRepository;
    private final UserRepository userRepository;
    private final UuidFileService uuidFileService;

    @Transactional
    public void applyDispatchCertify(User user, DispatchCertifyApplyRequestDto dispatchCertifyApplyRequestDto, List<MultipartFile> fileList) {
        if (fileList.isEmpty()) throw new BadRequestException(ResponseCode.FILE_NEEDED, "인증 사진이 필요합니다.");
        List<UuidFile> uuidFileList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            uuidFileList.add(uuidFileService.saveFile(file, FilePath.DISPATCH_CERTIFY));
        }

        closeAllUserCertify(user);

        user.changeRole(UserStatus.AWAIT);
        userRepository.save(user);

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

    @Transactional
    public void setNonCertified(User user) {
        closeAllUserCertify(user);
        user.changeRole(UserStatus.NON_CERTIFIED);
        userRepository.save(user);
    }

    public DispatchCertifyInfoResponseDto getDispatchCertifyInfo(Long dispatchCertifyId) {
        return DispatchCertifyInfoResponseDto.of(
                dispatchCertifyRepository.findById(dispatchCertifyId).orElseThrow(
                        () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 인증 신청 정보를 찾을 수 없습니다.")
                ));
    }

    public Page<DispatchCertifyInfoResponseDto> getDispatchCertifyInfoPageByUser(Long targetUserId, Pageable pageable) {
        return dispatchCertifyRepository.findAllByUser(
                userRepository.findById(targetUserId)
                        .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "목표 유저가 존재하지 않습니다.")),
                pageable)
                .map(DispatchCertifyInfoResponseDto::of);
    }

    public Page<DispatchCertifyInfoResponseDto> getDispatchCertifyInfoPageByUser(User user, Pageable pageable) {
        return dispatchCertifyRepository.findAllByUser(user, pageable).map(DispatchCertifyInfoResponseDto::of);
    }

    public Page<DispatchCertifyInfoResponseDto> getDispatchCertifyInfoPageByPermitStatus(PermitStatus permitStatus, Pageable pageable) {
        return dispatchCertifyRepository.findByPermitStatus(permitStatus, pageable).map(DispatchCertifyInfoResponseDto::of);
    }

    @Transactional
    public void changePermitStatus(Long dispatchCertifyId, PermitStatus permitStatus) {
        DispatchCertify dispatchCertify = dispatchCertifyRepository.findById(dispatchCertifyId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 인증 신청 정보를 찾을 수 없습니다."));
        dispatchCertify.setPermitStatus(permitStatus);
        dispatchCertifyRepository.save(dispatchCertify);

        if (permitStatus.equals(PermitStatus.CLOSED)) return;

        User user = dispatchCertify.getUser();
        user.changeRoleByDispatchCertify(dispatchCertify);

        closeAllUserCertifyExceptCurrentCertify(user, dispatchCertify);
        userRepository.save(user);
    }

    @Transactional
    public void deleteDispatchCertify(Long dispatchCertifyId) {
        DispatchCertify dispatchCertify = dispatchCertifyRepository.findById(dispatchCertifyId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 인증 신청 정보를 찾을 수 없습니다."));
        dispatchCertifyRepository.delete(dispatchCertify);
    }

    @Transactional
    public void deleteDispatchCertify(User user, Long dispatchCertifyId) {
        DispatchCertify dispatchCertify = dispatchCertifyRepository.findById(dispatchCertifyId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 인증 신청 정보를 찾을 수 없습니다."));
        if (!dispatchCertify.getUser().equals(user)) throw new UnauthorizedException(ResponseCode.GRANT_ROLE_NOT_ALLOWED, "해당 인증 신청 삭제에 대한 권한이 없습니다.");
        dispatchCertifyRepository.delete(dispatchCertify);
    }

    private void closeAllUserCertify(User user) {
        List<DispatchCertify> dispatchCertifyList = dispatchCertifyRepository.findAllByUser(user);
        for (DispatchCertify dispatchCertify : dispatchCertifyList) {
            dispatchCertify.setPermitStatus(PermitStatus.CLOSED);
        }
        dispatchCertifyRepository.saveAll(dispatchCertifyList);
    }

    private void closeAllUserCertifyExceptCurrentCertify(User user, DispatchCertify dispatchCertify) {
        List<DispatchCertify> dispatchCertifyList = dispatchCertifyRepository.findAllByUser(user);
        for (DispatchCertify certify : dispatchCertifyList) {
            if (certify.equals(dispatchCertify)) continue;
            certify.setPermitStatus(PermitStatus.CLOSED);
        }
        dispatchCertifyRepository.saveAll(dispatchCertifyList);
    }

}
