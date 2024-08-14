package com.on.server.domain.dispatchCertify.persentation;

import com.on.server.domain.dispatchCertify.application.DispatchCertifyService;
import com.on.server.domain.dispatchCertify.domain.PermitStatus;
import com.on.server.domain.dispatchCertify.dto.request.DispatchCertifyApplyRequestDto;
import com.on.server.domain.dispatchCertify.dto.response.DispatchCertifyInfoResponseDto;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispatch-certify")
@Tag(name = "교환/파견교 인증", description = "DispatchCertify API, 교환/파견교 인증 관련 API")
public class DispatchCertifyController {

    private final DispatchCertifyService dispatchCertifyService;
    private final SecurityService securityService;

    @Operation(summary = "교환/파견교 인증 신청")
    @PostMapping("/apply")
    public CommonResponse<Void> applyDispatchCertify(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DispatchCertifyApplyRequestDto dispatchCertifyApplyRequestDto,
            @RequestPart(value = "fileList") List<MultipartFile> fileList
    ) {
        dispatchCertifyService.applyDispatchCertify(securityService.getUserByUserDetails(userDetails), dispatchCertifyApplyRequestDto, fileList);
        return CommonResponse.success();
    }

    @Operation(summary = "교환/파견교 미정 확정")
    @PostMapping("/non_certified")
    public CommonResponse<Void> nonCertifiedDispatchCertify(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        dispatchCertifyService.setNonCertified(securityService.getUserByUserDetails(userDetails));
        return CommonResponse.success();
    }

    @Operation(summary = "교환/파견교 인증 정보 상세 조회(관리자만 사용 가능)")
    @GetMapping("/info/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<DispatchCertifyInfoResponseDto> getDispatchCertifyInfo(
            @PathVariable Long id
    ) {
        return CommonResponse.ok(dispatchCertifyService.getDispatchCertifyInfo(id));
    }

    @Operation(summary = "본인의 교환/파견교 인증 정보 페이지 조회")
    @PostMapping("/info/current")
    public CommonResponse<Page<DispatchCertifyInfoResponseDto>> getCurrentDispatchCertifyInfoPageByCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Pageable pageable
    ) {
        return CommonResponse.ok(dispatchCertifyService.getDispatchCertifyInfoPageByUser(securityService.getUserByUserDetails(userDetails), pageable));
    }

    @Operation(summary = "특정 사용자의 교환/파견교 인증 정보 페이지 조회(관리자만 사용 가능)")
    @PostMapping("/info/target-user/{targetUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<Page<DispatchCertifyInfoResponseDto>> getDispatchCertifyInfoPageByUser(
            @PathVariable Long targetUserId,
            @RequestBody Pageable pageable
    ) {
        return CommonResponse.ok(dispatchCertifyService.getDispatchCertifyInfoPageByUser(targetUserId, pageable));
    }

    @Operation(summary = "특정 상태의 교환/파견교 인증 정보 페이지 조회(관리자만 사용 가능)")
    @PostMapping("/info/{permitStatus}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<Page<DispatchCertifyInfoResponseDto>> getDispatchCertifyInfoPageByPermitStatus(
            @PathVariable PermitStatus permitStatus,
            @RequestBody Pageable pageable
    ) {
        return CommonResponse.ok(dispatchCertifyService.getDispatchCertifyInfoPageByPermitStatus(permitStatus, pageable));
    }

    @Operation(summary = "교환/파견교 인증 상태 변경(관리자만 사용 가능)")
    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<Void> changePermitStatus(
            @PathVariable Long id,
            @RequestBody PermitStatus permitStatus
    ) {
        dispatchCertifyService.changePermitStatus(id, permitStatus);
        return CommonResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<Void> deleteDispatchCertify(
            @PathVariable Long id
    ) {
        dispatchCertifyService.deleteDispatchCertify(id);
        return CommonResponse.success();
    }

    @Operation(summary = "본인의 교환/파견교 인증 정보 삭제")
    @DeleteMapping("/delete/self/{id}")
    public CommonResponse<Void> deleteSelfDispatchCertify(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ) {
        dispatchCertifyService.deleteDispatchCertify(securityService.getUserByUserDetails(userDetails), id);
        return CommonResponse.success();
    }

}
