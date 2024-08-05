package com.on.server.domain.companyPost.application;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.image.domain.Image;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyPostService {

    private final CompanyPostRepository companyPostRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public CompanyPostService(CompanyPostRepository companyPostRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.companyPostRepository = companyPostRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    // 1. 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<CompanyPostResponseDTO> getAllCompanyPosts() {
        return companyPostRepository.findAll().stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. 특정 게시글 조회
    @Transactional(readOnly = true)
    public CompanyPostResponseDTO getCompanyPostById(Long companyPostId) {
        CompanyPost companyPost = companyPostRepository.findById(companyPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + companyPostId));
        return mapToCompanyPostResponseDTO(companyPost);
    }

    // 3. 새로운 게시글 작성
    public CompanyPostResponseDTO createCompanyPost(CompanyPostRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + requestDTO.getUserId()));

        List<Image> images = requestDTO.getImageIds() != null ?
                requestDTO.getImageIds().stream()
                        .map(imageId -> imageRepository.findById(imageId)
                                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다. ID: " + imageId)))
                        .collect(Collectors.toList()) : Collections.emptyList();

        CompanyPost companyPost = CompanyPost.builder()
                .user(user)
                .ageAnonymous(requestDTO.isAgeAnonymous())
                .universityAnonymous(requestDTO.isUniversityAnonymous())
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .departurePlace(requestDTO.getDeparturePlace())
                .arrivePlace(requestDTO.getArrivePlace())
                .recruitNumber(requestDTO.getRecruitNumber())
                .schedulePeriodDay(requestDTO.getSchedulePeriodDay())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .images(images)
                .build();

        companyPostRepository.save(companyPost);

        return mapToCompanyPostResponseDTO(companyPost);
    }

    // 4. 특정 사용자가 작성한 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<CompanyPostResponseDTO> getCompanyPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        return companyPostRepository.findByUser(user).stream()
                .map(this::mapToCompanyPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    public void deleteCompanyPost(Long userId, Long companyPostId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        CompanyPost companyPost = companyPostRepository.findById(companyPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + companyPostId));

        if (!companyPost.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        companyPostRepository.delete(companyPost);
    }

    // CompanyPost 엔티티를 CompanyPostResponseDto로 매핑하는 메서드
    private CompanyPostResponseDTO mapToCompanyPostResponseDTO(CompanyPost companyPost) {
        return CompanyPostResponseDTO.builder()
                .companyPostId(companyPost.getCompanyPostId())
                .userId(companyPost.getUser().getUserId())
                .ageAnonymous(companyPost.isAgeAnonymous())
                .universityAnonymous(companyPost.isUniversityAnonymous())
                .title(companyPost.getTitle())
                .content(companyPost.getContent())
                .departurePlace(companyPost.getDeparturePlace())
                .arrivePlace(companyPost.getArrivePlace())
                .recruitNumber(companyPost.getRecruitNumber())
                .schedulePeriodDay(companyPost.getSchedulePeriodDay())
                .startDate(companyPost.getStartDate())
                .endDate(companyPost.getEndDate())
                .imageIdList(companyPost.getImages().stream().map(Image::getImageId).collect(Collectors.toList()))
                .build();
    }
}