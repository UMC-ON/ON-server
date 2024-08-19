package com.on.server.domain.home.presentation;

import com.on.server.domain.home.application.HomeService;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
@Tag(name = "Home", description = "Home API")
public class HomeController {

    private final HomeService homeService;
    private final SecurityService securityService;

    // [GET] 최신 정보글 조회
//    @GetMapping("/info/list")
//    @Operation(summary = "최신 정보글 조회 API", description = "홈 화면에서 최신 정보글을 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getInfoBoardList(@AuthenticationPrincipal UserDetails userDetails) {
//
//        User user = securityService.getUserByUserDetails(userDetails);
//
//        return new CommonResponse<>(ResponseCode.SUCCESS, homeService.getInfoBoardList(user));
//    }

    // [GET] 최신 자유글 조회
//    @GetMapping("/free/list")
//    @Operation(summary = "최신 자유글 조회 API", description = "홈 화면에서 최신 자유글을 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getFreeBoardList(@AuthenticationPrincipal UserDetails userDetails) {
//
//        User user = securityService.getUserByUserDetails(userDetails);
//
//        return new CommonResponse<>(ResponseCode.SUCCESS, homeService.getFreeBoardList(user));
//    }

    // [GET] 내 주변 동행글 조회
//    @GetMapping("/company/list")
//    @Operation(summary = "내 주변 동행글 조회 API", description = "홈 화면에서 내 주변 동행글 조회하는 API 입니다.")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> getCompanyBoardList(@AuthenticationPrincipal UserDetails userDetails) {
//
//        User user = securityService.getUserByUserDetails(userDetails);
//
//        return new CommonResponse<>(ResponseCode.SUCCESS, homeService.getCompanyBoardList(user));
//    }

}
