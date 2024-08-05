package com.on.server.domain.companyPost.application;

import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.companyPost.dto.CompanyPostRequestDTO;
import com.on.server.domain.companyPost.dto.CompanyPostResponseDTO;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyPostService {

    private final CompanyPostRepository companyPostRepository;
    private final UserRepository userRepository;
    //private final ImageRepository imageRepository;

    // 1. 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<CompanyPostResponseDTO> getAllCompanyPosts() {
        return null;
    }

    // 2. 특정 게시글 조회
    @Transactional(readOnly = true)
    public CompanyPostResponseDTO getCompanyPostById(Long companyPostId) {
        return null;
    }

    // 3. 새로운 게시글 작성
    public CompanyPostResponseDTO createCompanyPost(CompanyPostRequestDTO requestDTO) {
        return null;
    }

    // 4. 특정 사용자가 작성한 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<CompanyPostResponseDTO> getCompanyPostsByUserId(Long userId) {
        return null;
    }

    // 5. 특정 게시글 삭제
    public void deleteCompanyPost(Long userId, Long companyPostId) {

    }
}