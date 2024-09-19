package com.on.server.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 익명 여부
    private boolean isAnonymous;

    // 파견교 공개 여부
    private boolean isAnonymousUniv;
}