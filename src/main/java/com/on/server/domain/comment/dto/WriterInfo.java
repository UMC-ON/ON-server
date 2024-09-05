package com.on.server.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterInfo {
    // 작성자 ID
    private Long id;

    // 작성자 닉네임
    private String nickname;
}
