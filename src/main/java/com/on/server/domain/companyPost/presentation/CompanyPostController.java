package com.on.server.domain.companyPost.presentation;

import com.on.server.domain.companyPost.application.CompanyPostService;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "동행구하기글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/company-posts")
public class CompanyPostController {

    private final CompanyPostService companyPostService;

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
    @PostMapping
    public ResponseEntity<CompanyPostResponseDTO> createCompanyPost(@RequestBody CompanyPostRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
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
}

