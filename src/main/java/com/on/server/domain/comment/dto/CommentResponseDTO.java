package com.on.server.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    // 댓글 ID
    private Long commentId;

    // 작성자 ID
    private Long userId;

    // 게시글 ID
    private Long postId;

    // 익명 여부
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;
}
