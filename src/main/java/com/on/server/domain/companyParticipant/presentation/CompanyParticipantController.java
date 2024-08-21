package com.on.server.domain.companyParticipant.presentation;

import com.on.server.domain.companyParticipant.application.CompanyParticipantService;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantRequestDTO;
import com.on.server.domain.companyParticipant.dto.CompanyParticipantResponseDTO;
import com.on.server.domain.companyParticipant.domain.CompanyParticipantStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "동행 신청하기")
@RestController
@RequestMapping("/api/v1/company-participant")
@RequiredArgsConstructor
public class CompanyParticipantController {

    private final CompanyParticipantService companyParticipantService;

    @Operation(summary = "동행구하기글에 동행 신청")
    @PostMapping("/apply")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    public ResponseEntity<CompanyParticipantResponseDTO> applyToCompanyPost(@RequestBody CompanyParticipantRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        CompanyParticipantResponseDTO responseDTO = companyParticipantService.applyToCompanyPost(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
