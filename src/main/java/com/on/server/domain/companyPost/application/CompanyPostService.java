package com.on.server.domain.companyPost.application;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final UuidFileService uuidFileService;

    // 필터링 기능 추가
    public Page<CompanyPostResponseDTO> getFilteredCompanyPosts(LocalDate startDate, LocalDate endDate, Gender gender, String country, Pageable pageable) {
        Page<CompanyPost> posts = companyPostRepository.findFilteredCompanyPostsWithoutCountry(startDate, endDate, gender, pageable);

        if (country != null && !country.isEmpty()) {
            posts = posts.stream()
                    .filter(post -> post.getTravelArea().stream()
                            .anyMatch(area -> {
                                String firstWord = area.split(" ")[0];  // travelArea의 첫 번째 단어 추출
                                return firstWord.equalsIgnoreCase(country);
                            }))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
        }

        return posts.map(CompanyPostResponseDTO::from);
    }

    // 1. 모든 게시글 조회
    public Page<CompanyPostResponseDTO> getAllCompanyPosts(Pageable pageable) {
        return companyPostRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(CompanyPostResponseDTO::from);
    }

    // 2. 특정 게시글 조회
    public List<CompanyPostResponseDTO> getCompanyPostById(Long companyPostId) {
        return companyPostRepository.findById(companyPostId)
                .stream()
                .map(CompanyPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 3. 새로운 게시글 작성
    @Transactional
    public CompanyPostResponseDTO createCompanyPost(User user, CompanyPostRequestDTO requestDTO, List<MultipartFile> imageFiles) {

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

        return CompanyPostResponseDTO.from(companyPost);
    }

    // 4. 특정 사용자가 작성한 모든 게시글 조회
    public Page<CompanyPostResponseDTO> getCompanyPostsByUser(User user, Pageable pageable) {
        return companyPostRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                .map(CompanyPostResponseDTO::from);
    }

    // 5. 특정 게시글 삭제
    @Transactional
    public void deleteCompanyPost(User user, Long companyPostId) {
        CompanyPost companyPost = companyPostRepository.findById(companyPostId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + companyPostId));

        if (!companyPost.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ResponseCode.GRANT_ROLE_NOT_ALLOWED, "삭제 권한이 없습니다.");
        }

        // 연관된 이미지 삭제
        companyPost.getImages().forEach(uuidFileService::deleteFile);

        companyPostRepository.delete(companyPost);
    }

    // 6. 최신 4개의 동행 구하기 게시글 조회
    public List<CompanyPostResponseDTO> getRecentCompanyPosts() {
        return companyPostRepository.findTop4ByOrderByCreatedAtDesc().stream()
                .map(CompanyPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 내 주변 동행글 조회
    public List<CompanyPostResponseDTO> getNearbyCompanyPostsByLikeTravelArea(Long companyPostId) {

        // 여러 개의 travel area 중에서 첫 번째만 선택
        CompanyPostResponseDTO post = getCompanyPostById(companyPostId).get(0);

        // 국가만 일치해도 조회되도록 국가 부분만 추출
        String firstCountry = post.getTravelArea().get(0).split(" ")[0];

        // 국가와 일치하는 다른 게시글을 조회, 현재 게시글은 제외
        List<CompanyPost> nearbyPosts = companyPostRepository.findTop5ByTravelAreaLike(firstCountry, companyPostId, PageRequest.of(0, 5));

        return nearbyPosts.stream()
                .map(CompanyPostResponseDTO::from)
                .collect(Collectors.toList());
    }
}