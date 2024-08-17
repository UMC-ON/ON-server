package com.on.server.domain.companyPost.presentation;

import com.on.server.domain.companyPost.application.CompanyPostService;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "동행구하기글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company-post")
public class CompanyPostController {

    private final CompanyPostService companyPostService;

    // 필터링 기능 추가
    @Operation(summary = "필터링된 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/filter")
    public ResponseEntity<List<CompanyPostResponseDTO>> getFilteredCompanyPosts(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String country,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<CompanyPostResponseDTO> filteredPosts = companyPostService.getFilteredCompanyPosts(startDate, endDate, gender, country);
        return ResponseEntity.ok(filteredPosts);
    }

    // 1. 모든 게시글 조회
    @Operation(summary = "모든 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping
    public ResponseEntity<List<CompanyPostResponseDTO>> getAllCompanyPosts(@AuthenticationPrincipal UserDetails userDetails
    ) {
        List<CompanyPostResponseDTO> posts = companyPostService.getAllCompanyPosts();
        return ResponseEntity.ok(posts);
    }


    // 2. 특정 게시글 조회
    @Operation(summary = "특정 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{companyPostId}")
    public ResponseEntity<CompanyPostResponseDTO> getCompanyPostById(@PathVariable Long companyPostId, @AuthenticationPrincipal UserDetails userDetails) {
        CompanyPostResponseDTO post = companyPostService.getCompanyPostById(companyPostId);
        return ResponseEntity.ok(post);
    }

    // 3. 새로운 게시글 작성
    @Operation(summary = "새로운 동행구하기 게시글 작성")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyPostResponseDTO> createCompanyPost(@ModelAttribute CompanyPostRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // 현재 인증된 사용자의 ID를 DTO에 설정
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            requestDTO.setUserId(user.getId());
        }

        CompanyPostResponseDTO createdPost = companyPostService.createCompanyPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 4. 마이페이지에서 자기가 작성한 모든 게시글 조회
    @Operation(summary = "자기가 작성한 모든 동행구하기 게시글 모아보기(조회)")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CompanyPostResponseDTO>> getCompanyPostsByUserId(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        List<CompanyPostResponseDTO> posts = companyPostService.getCompanyPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 5. 마이페이지에서 자기가 작성한 특정 게시글 삭제
    @Operation(summary = "자기가 작성한 특정 동행구하기 게시글 삭제")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @DeleteMapping("/user/{userId}/{companyPostId}")
    public ResponseEntity<Void> deleteCompanyPost(@PathVariable Long userId, @PathVariable Long companyPostId, @AuthenticationPrincipal UserDetails userDetails) {
        companyPostService.deleteCompanyPost(userId, companyPostId);
        return ResponseEntity.noContent().build();
    }

    // 6. 최신 4개의 동행 구하기 게시글 조회
    @Operation(summary = "최신 4개의 동행구하기글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/recent")
    public ResponseEntity<List<CompanyPostResponseDTO>> getRecentCompanyPosts(@AuthenticationPrincipal UserDetails userDetails) {
        List<CompanyPostResponseDTO> recentPosts = companyPostService.getRecentCompanyPosts();
        return ResponseEntity.ok(recentPosts);
    }
}

