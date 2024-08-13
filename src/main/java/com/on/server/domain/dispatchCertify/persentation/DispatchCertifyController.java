package com.on.server.domain.dispatchCertify.persentation;

import com.on.server.domain.dispatchCertify.application.DispatchCertifyService;
import com.on.server.domain.dispatchCertify.dto.request.DispatchCertifyApplyRequestDto;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/apply")
    public CommonResponse<Void> applyDispatchCertify(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DispatchCertifyApplyRequestDto dispatchCertifyApplyRequestDto,
            @RequestPart(value = "fileList") List<MultipartFile> fileList
    ) {
        dispatchCertifyService.applyDispatchCertify(securityService.getUserByUserDetails(userDetails), dispatchCertifyApplyRequestDto, fileList);
        return CommonResponse.success();
    }
}
