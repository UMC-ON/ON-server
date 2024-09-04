package com.on.server.domain.board.application;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    // 특정 게시판의 모든 게시글 조회
    public List<PostResponseDTO> getAllPostsByBoardType(BoardType boardType) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        return board.getPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> PostResponseDTO.from(post, true))
                .collect(Collectors.toList());
    }

    // 사용자가 특정 게시판에 작성한 모든 게시글 조회
    public List<PostResponseDTO> getPostsByUserIdAndBoardType(User user, BoardType boardType) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByUserAndBoardOrderByCreatedAtDesc(user, board);
        return posts.stream()
                .map(post -> PostResponseDTO.from(post, true))
                .collect(Collectors.toList());
    }

    // 특정 게시판의 최신 게시글 4개 조회
    public List<PostResponseDTO> getLatestPostsByBoardType(BoardType boardType) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findTop4ByBoardOrderByCreatedAtDesc(board);
        return posts.stream()
                .map(post -> PostResponseDTO.from(post, true))
                .collect(Collectors.toList());
    }
}
