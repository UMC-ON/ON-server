package com.on.server.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDTO {

    // 게시판 ID
    private Long boardId;

    // 게시판 이름
    private String name;
}
