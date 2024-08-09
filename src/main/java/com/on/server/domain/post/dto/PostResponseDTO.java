package com.on.server.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    // 게시글 ID
    private Long postId;

    // 게시판 ID
    private Long boardId;

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