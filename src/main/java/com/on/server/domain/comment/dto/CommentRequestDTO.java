package com.on.server.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDTO {

    // 작성자 ID
    private Long userId;

    // 게시글 ID
    private Long postId;

    // 익명 여부
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;

}