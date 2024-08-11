package com.on.server.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CommentRequestDTO {

    // 작성자 ID
    private Long userId;

    // 익명 여부
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;

}