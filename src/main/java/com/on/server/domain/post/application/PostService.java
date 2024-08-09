package com.on.server.domain.post.application;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
//    private final ImageRepository imageRepository;


    // 1. 특정 게시판의 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPostsByBoardId(Long boardId) {
        return null;
    }

    // 2. 특정 게시판에 새로운 게시글 작성
    public PostResponseDTO createPost(Long boardId, PostRequestDTO postRequestDTO) {
        return null;
    }

    // 3. 특정 게시글 조회
    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long boardId, Long postId) {
        return null;
    }

    // 4. 특정 사용자가 특정 게시판에 작성한 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByUserIdAndBoardId(Long userId, Long boardId) {
        return null;
    }

    // 5. 특정 게시글 삭제
    public void deletePost(Long userId, Long boardId, Long postId) {

    }
}

