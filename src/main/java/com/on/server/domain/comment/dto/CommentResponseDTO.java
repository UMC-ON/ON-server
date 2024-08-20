package com.on.server.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    // 댓글 ID
    private Long commentId;

    // 답글 ID
    private Long replyId;

    // 게시글 ID
    private Long postId;

    // 작성자 정보
    private WriterInfo writerInfo;

    // 익명 여부
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;

    // 답글 개수
    private Integer replyCount;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WriterInfo {
        // 작성자 ID
        private Long id;

        // 작성자 닉네임
        private String nickname;
    }
}
