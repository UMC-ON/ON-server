package com.on.server.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {

    // 작성자 ID
    private Long userId;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 익명 여부
    private boolean isAnonymous;

    // 파견교 공개 여부
    private boolean isAnonymousUniv;

    // 이미지 파일 리스트
    private List<MultipartFile> imageFiles;
}