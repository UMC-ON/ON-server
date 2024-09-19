package com.on.server.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDTO {

    // 익명 여부
    @JsonProperty("anonymous")
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;

}