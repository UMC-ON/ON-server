package com.on.server.domain.scrap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrapRequestDTO {
    //마켓글 ID
    private Long marketPostId;

    //유저 ID
    private Long userId;
}
