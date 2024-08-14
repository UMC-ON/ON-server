package com.on.server.domain.post.dto;

import com.on.server.domain.board.domain.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    // 게시판 타입
    private BoardType boardType;

    // 게시글 ID
    private Long postId;

    // 작성자 ID
    private Long userId;

    // 작성자 닉네임
    private String userNickname;

    // 익명 여부
    private boolean isAnonymous;

    // 파견국가
    private String country;

    // 파견교
    private String dispatchedUniversity;

    // 파견교 공개 여부
    private boolean isAnonymousUniv;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 이미지 ID 리스트
    private List<Long> imageIdList;

    // 생성 시간
    private LocalDateTime createdAt;

    // 댓글 개수 (선택적)
    private Integer commentCount;
}