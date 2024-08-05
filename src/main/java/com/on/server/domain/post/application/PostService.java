package com.on.server.domain.post.application;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.image.domain.Image;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, BoardRepository boardRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.imageRepository = imageRepository;
    }

    // 1. 특정 게시판의 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPostsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        return board.getPosts().stream()
                .map(this::mapToPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. 특정 게시판에 새로운 게시글 작성
    public PostResponseDTO createPost(Long boardId, PostRequestDTO postRequestDTO) {
        User user = userRepository.findById(postRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Image> images = postRequestDTO.getImageIdList() != null ?
                postRequestDTO.getImageIdList().stream()
                        .map(imageId -> imageRepository.findById(imageId)
                                .orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다.")))
                        .collect(Collectors.toList());

        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .content(postRequestDTO.getContent())
                .isAnonymous(postRequestDTO.isAnonymous())
                .isAnonymousUniv(postRequestDTO.isAnonymousUniv())
                .board(board)
                .user(user)
                .images(images)
                .build();

        postRepository.save(post);

        return mapToPostResponseDTO(post);
    }

    // 3. 특정 게시글 조회
    @Transactional(readOnly = true)
    public PostResponseDTO getPostById(Long boardId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!post.getBoard().getBoardId().equals(boardId)) {
            throw new RuntimeException("해당 게시판에 게시글이 존재하지 않습니다.");
        }
        return mapToPostResponseDTO(post);
    }

    // 4. 특정 사용자가 특정 게시판에 작성한 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByUserIdAndBoardId(Long userId, Long boardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByUserAndBoard(user, board);
        return posts.stream()
                .map(this::mapToPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    public void deletePost(Long userId, Long boardId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUserId().equals(userId) || !post.getBoard().getBoardId().equals(boardId)) {
            throw new RuntimeException("해당 게시판에 게시글이 존재하지 않습니다.");
        }

        postRepository.delete(post);
    }

    // Post 엔티티를 PostResponseDTO로 매핑하는 메서드
    private PostResponseDTO mapToPostResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .boardId(post.getBoard().getBoardId())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .isAnonymous(post.getIsAnonymous())
                .isAnonymousUniv(post.getIsAnonymousUniv())
                .imageIdList(post.getImages().stream().map(Image::getImageId).collect(Collectors.toList()))
                .build();
    }
}

