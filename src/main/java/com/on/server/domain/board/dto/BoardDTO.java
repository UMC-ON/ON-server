package com.on.server.domain.board.dto;

import com.on.server.domain.board.domain.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDTO {

    // 게시판 ID
    private Long boardId;

    // 게시판 타입
    private BoardType type;
}
