package com.on.server.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
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

    // 이미지 ID 리스트
    private List<Long> imageIdList;
}