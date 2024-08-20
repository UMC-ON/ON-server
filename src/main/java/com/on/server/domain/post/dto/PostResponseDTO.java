package com.on.server.domain.post.dto;

import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.user.domain.UserStatus;
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

    // 작성자 정보
    private WriterInfo writerInfo;

    // 익명 여부
    private boolean isAnonymous;

    // 파견교 공개 여부
    private boolean isAnonymousUniv;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 이미지 ID 리스트
    private List<String> imageUrls;

    // 작성 시간
    private LocalDateTime createdAt;

    // 댓글 개수
    private Integer commentCount;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WriterInfo {
        // 작성자 ID
        private Long id;

        // 작성자 닉네임
        private String nickname;

        // 파견국가
        private String country;

        // 파견교
        private String dispatchedUniversity;

        // 작성자 상태
        private UserStatus userStatus;
    }
}