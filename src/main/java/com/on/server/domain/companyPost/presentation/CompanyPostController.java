package com.on.server.domain.companyPost.presentation;

import com.on.server.domain.companyPost.application.CompanyPostService;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "동행구하기글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company-post")
public class CompanyPostController {

    private final CompanyPostService companyPostService;
    private final SecurityService securityService;

    // 필터링 기능 추가
    @Operation(summary = "필터링된 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/filter")
    public ResponseEntity<Page<CompanyPostResponseDTO>> getFilteredCompanyPosts(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String country,
            @ParameterObject Pageable pageable) {

        Page<CompanyPostResponseDTO> filteredPosts = companyPostService.getFilteredCompanyPosts(startDate, endDate, gender, country, pageable);
        return ResponseEntity.ok(filteredPosts);
    }

    // 1. 모든 게시글 조회
    @Operation(summary = "모든 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping
    public ResponseEntity<Page<CompanyPostResponseDTO>> getAllCompanyPosts(@ParameterObject Pageable pageable) {
        Page<CompanyPostResponseDTO> posts = companyPostService.getAllCompanyPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 게시글 조회
    @Operation(summary = "특정 동행구하기 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{companyPostId}")
    public ResponseEntity<List<CompanyPostResponseDTO>> getCompanyPostById(@PathVariable Long companyPostId) {
        List<CompanyPostResponseDTO> post = companyPostService.getCompanyPostById(companyPostId);
        return ResponseEntity.ok(post);
    }

    // 3. 새로운 게시글 작성
    @Operation(summary = "새로운 동행구하기 게시글 작성")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyPostResponseDTO> createCompanyPost(
            @RequestPart("requestDTO") CompanyPostRequestDTO requestDTO,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        CompanyPostResponseDTO createdPost = companyPostService.createCompanyPost(user, requestDTO, imageFiles);
        return ResponseEntity.ok(createdPost);
    }

    // 4. 마이페이지에서 자기가 작성한 모든 게시글 조회
    @Operation(summary = "자기가 작성한 모든 동행구하기 게시글 모아보기(조회)")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/user")
    public ResponseEntity<Page<CompanyPostResponseDTO>> getCompanyPostsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                              @ParameterObject Pageable pageable) {
        User user = securityService.getUserByUserDetails(userDetails);

        Page<CompanyPostResponseDTO> posts = companyPostService.getCompanyPostsByUser(user, pageable);
        return ResponseEntity.ok(posts);
    }

    // 5. 마이페이지에서 자기가 작성한 특정 게시글 삭제
    @Operation(summary = "자기가 작성한 특정 동행구하기 게시글 삭제")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @DeleteMapping("/user/{companyPostId}")
    public ResponseEntity<Void> deleteCompanyPost(@PathVariable Long companyPostId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityService.getUserByUserDetails(userDetails);

        companyPostService.deleteCompanyPost(user, companyPostId);
        return ResponseEntity.ok().build();
    }

    // 6. 최신 4개의 동행 구하기 게시글 조회
    @Operation(summary = "최신 4개의 동행구하기글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/recent")
    public ResponseEntity<List<CompanyPostResponseDTO>> getRecentCompanyPosts() {
        List<CompanyPostResponseDTO> recentPosts = companyPostService.getRecentCompanyPosts();
        return ResponseEntity.ok(recentPosts);
    }

    @Operation(summary = "내 주변 동행글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{companyPostId}/nearby")
    public ResponseEntity<List<CompanyPostResponseDTO>> getNearbyCompanyPostsByLikeTravelArea(@PathVariable Long companyPostId) {
        List<CompanyPostResponseDTO> nearbyPosts = companyPostService.getNearbyCompanyPostsByLikeTravelArea(companyPostId);
        return ResponseEntity.ok(nearbyPosts);
    }
}

