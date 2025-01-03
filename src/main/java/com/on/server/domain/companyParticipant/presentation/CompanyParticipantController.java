package com.on.server.domain.companyParticipant.presentation;

import com.on.server.domain.companyParticipant.application.CompanyParticipantService;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantRequestDTO;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "동행 신청하기")
@RestController
@RequestMapping("/api/v1/company-participant")
@RequiredArgsConstructor
public class CompanyParticipantController {

    private final CompanyParticipantService companyParticipantService;
    private final SecurityService securityService;

    @Operation(summary = "동행구하기글에 동행 신청")
    @PostMapping("/apply")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<CompanyParticipantResponseDTO> applyToCompanyPost (
            @RequestBody CompanyParticipantRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetails userDetails)
    {

        User user = securityService.getUserByUserDetails(userDetails);

        CompanyParticipantResponseDTO responseDTO = companyParticipantService.applyToCompanyPost(user, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "특정 채팅방의 동행 신청자의 동행 신청 상태 확인")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/status/{userId}/{chattingRoomId}")
    public ResponseEntity<CompanyParticipantResponseDTO> getCompanyParticipantStatus(@PathVariable Long userId,
                                                                                     @PathVariable Long chattingRoomId) {

        CompanyParticipantResponseDTO status = companyParticipantService.getCompanyParticipantStatus(userId, chattingRoomId);
        return ResponseEntity.ok(status);
    }
}
