package com.on.server.domain.companyPost.application;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.aws.s3.uuidFile.domain.repository.UuidFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyPostService {

    private final CompanyPostRepository companyPostRepository;
    private final UserRepository userRepository;
    private final UuidFileService uuidFileService;
    private final UuidFileRepository uuidFileRepository;

    // 필터링 기능 추가
    public List<CompanyPostResponseDTO> getFilteredCompanyPosts(LocalDate startDate, LocalDate endDate, Gender gender, String country) {
        List<CompanyPost> filteredPosts = companyPostRepository.findFilteredCompanyPosts(startDate, endDate, gender, country);
        return filteredPosts.stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 1. 모든 게시글 조회
    public List<CompanyPostResponseDTO> getAllCompanyPosts() {
        return companyPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. 특정 게시글 조회
    public List<CompanyPostResponseDTO> getCompanyPostById(Long companyPostId) {
        return companyPostRepository.findById(companyPostId)
                .stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. 새로운 게시글 작성
    @Transactional
    public CompanyPostResponseDTO createCompanyPost(CompanyPostRequestDTO requestDTO, List<MultipartFile> imageFiles) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + requestDTO.getUserId()));

        List<UuidFile> uploadedImages = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            uploadedImages = imageFiles.stream()
                    .map(file -> uuidFileService.saveFile(file, FilePath.POST))
                    .collect(Collectors.toList());
        }

        CompanyPost companyPost = CompanyPost.builder()
                .user(user)
                .ageAnonymous(requestDTO.isAgeAnonymous())
                .universityAnonymous(requestDTO.isUniversityAnonymous())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .travelArea(requestDTO.getTravelArea())
                .totalRecruitNumber(requestDTO.getTotalRecruitNumber())
                .schedulePeriodDay(requestDTO.getSchedulePeriodDay())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .currentRecruitNumber(0L) // 모집 인원을 초기화
                .currentCountry(requestDTO.getCurrentCountry())
                .isRecruitCompleted(false)
                .images(uploadedImages) // 이미지 리스트를 초기화된 리스트로 설정
                .build();

        companyPost = companyPostRepository.save(companyPost);

        return mapToCompanyPostResponseDTO(companyPost);
    }

    // 4. 특정 사용자가 작성한 모든 게시글 조회
    public List<CompanyPostResponseDTO> getCompanyPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        return companyPostRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    @Transactional
    public void deleteCompanyPost(Long userId, Long companyPostId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        CompanyPost companyPost = companyPostRepository.findById(companyPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + companyPostId));

        if (!companyPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 연관된 이미지 삭제
        companyPost.getImages().forEach(uuidFileService::deleteFile);

        companyPostRepository.delete(companyPost);
    }

    // 6. 최신 4개의 동행 구하기 게시글 조회
    public List<CompanyPostResponseDTO> getRecentCompanyPosts() {
        return companyPostRepository.findTop4ByOrderByCreatedAtDesc().stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 내 주변 동행글 조회
    public List<CompanyPostResponseDTO> getNearbyCompanyPostsByLikeTravelArea(Long companyPostId) {
        CompanyPostResponseDTO post = getCompanyPostById(companyPostId).get(0);  // 첫 번째 요소 선택
        String firstCountry = post.getTravelArea().get(0); // 첫 번째 국가를 기준으로 설정
        List<CompanyPost> nearbyPosts = companyPostRepository.findTop5ByTravelAreaLike(firstCountry, companyPostId); // 내 게시글 제외
        return nearbyPosts.stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // CompanyPost 엔티티를 CompanyPostResponseDto로 매핑하는 메서드
    private CompanyPostResponseDTO mapToCompanyPostResponseDTO(CompanyPost companyPost) {
        return CompanyPostResponseDTO.builder()
                .companyPostId(companyPost.getId())
                .userId(companyPost.getUser().getId())
                .age(companyPost.getUser().getAge())
                .ageAnonymous(companyPost.isAgeAnonymous())
                .dispatchedUniversity(companyPost.getUser().getDispatchedUniversity())
                .nickname(companyPost.getUser().getNickname())
                .gender(companyPost.getUser().getGender())
                .universityAnonymous(companyPost.isUniversityAnonymous())
                .title(companyPost.getTitle())
                .content(companyPost.getContent())
                .travelArea(companyPost.getTravelArea())
                .currentRecruitNumber(companyPost.getCurrentRecruitNumber())  // 현재 모집 인원 수
                .totalRecruitNumber(companyPost.getTotalRecruitNumber())  // 전체 모집 인원 수
                .isRecruitCompleted(companyPost.isRecruitCompleted())
                .schedulePeriodDay(companyPost.getSchedulePeriodDay())
                .startDate(companyPost.getStartDate())
                .endDate(companyPost.getEndDate())
                .currentCountry(companyPost.getCurrentCountry())
                .createdAt(companyPost.getCreatedAt())
                .imageUrls(companyPost.getImages().stream()
                        .map(UuidFile::getFileUrl)
                        .collect(Collectors.toList()))  // 이미지 URL 리스트
                .build();
    }
}