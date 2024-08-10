package com.on.server.domain.board.domain.repository;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByType(BoardType type);
}
