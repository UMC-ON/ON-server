package com.on.server.domain.post.application;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.aws.s3.uuidFile.domain.repository.UuidFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UuidFileService uuidFileService;

    // 1. 특정 게시판의 모든 게시글 조회
    public List<PostResponseDTO> getAllPostsByBoardType(BoardType boardType) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        return board.getPosts().stream()
                .map(post -> mapToPostResponseDTO(post, true)) // 댓글 수 포함
                .collect(Collectors.toList());
    }

    // 2. 특정 게시판에 새로운 게시글 작성
    @Transactional
    public PostResponseDTO createPost(BoardType boardType, PostRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        // 이미지 파일 처리
        List<UuidFile> uploadedImages = new ArrayList<>();
        if (requestDTO.getImageFiles() != null && !requestDTO.getImageFiles().isEmpty()) {
            uploadedImages = requestDTO.getImageFiles().stream()
                    .map(file -> uuidFileService.saveFile(file, FilePath.POST))
                    .collect(Collectors.toList());
        }

        // Post 객체 생성
        Post post = Post.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.isAnonymous())
                .isAnonymousUniv(requestDTO.isAnonymousUniv())
                .board(board)
                .images(uploadedImages)
                .build();

        // 게시글 저장
        post = postRepository.save(post);

        return mapToPostResponseDTO(post, true);
    }

    // 3. 특정 게시글 조회
    public PostResponseDTO getPostById(BoardType boardType, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!post.getBoard().getType().equals(boardType)) {
            throw new RuntimeException("해당 게시판에 게시글이 존재하지 않습니다.");
        }
        return mapToPostResponseDTO(post, true); // 댓글 수 포함
    }

    // 4. 특정 사용자가 특정 게시판에 작성한 모든 게시글 조회
    public List<PostResponseDTO> getPostsByUserIdAndBoardType(Long userId, BoardType boardType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByUserAndBoard(user, board);
        return posts.stream()
                .map(post -> mapToPostResponseDTO(post, true)) // 댓글 수 포함
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    @Transactional
    public void deletePost(Long userId, BoardType boardType, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId) || !post.getBoard().getType().equals(boardType)) {
            throw new RuntimeException("해당 게시판에 게시글이 존재하지 않습니다.");
        }

        // 게시글에 연결된 이미지 삭제
        List<UuidFile> images = post.getImages();
        if (images != null) {
            for (UuidFile image : images) {
                uuidFileService.deleteFile(image);
            }
        }

        postRepository.delete(post);
    }

    // 게시글 검색 기능
    @Transactional(readOnly = true)
    public List<PostResponseDTO> searchPosts(String keyword) {
        List<Post> posts = postRepository.searchPosts(keyword);
        return posts.stream()
                .map(post -> mapToPostResponseDTO(post, true))
                .collect(Collectors.toList());
    }

    // 국가 필터링 메서드
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByCountryAndBoardType(BoardType boardType, String country) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        // Board 객체와 국가를 기준으로 게시글을 조회합니다.
        List<Post> posts = postRepository.findByBoardAndCountryInTitleOrContent(board, country);

        return posts.stream()
                .map(post -> mapToPostResponseDTO(post, true))
                .collect(Collectors.toList());
    }

    // 특정 게시판의 최신 게시글 4개 조회
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getLatestPostsByBoardType(BoardType boardType) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findTop4ByBoardOrderByCreatedAtDesc(board);
        return posts.stream()
                .map(post -> mapToPostResponseDTO(post, true))
                .collect(Collectors.toList());
    }

    // Post 엔티티를 PostResponseDTO로 매핑하는 메서드
    private PostResponseDTO mapToPostResponseDTO(Post post, boolean includeCommentCount) {
        User user = post.getUser();
        Set<UserStatus> roles = user.getRoles();
        UserStatus userStatus = roles.iterator().next();

        int commentCount = (post.getComments() != null) ? post.getComments().size() : 0;

        PostResponseDTO.WriterInfo writerInfo = PostResponseDTO.WriterInfo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .country(user.getCountry())
                .dispatchedUniversity(user.getDispatchedUniversity())
                .userStatus(userStatus)
                .build();

        return PostResponseDTO.builder()
                .postId(post.getId())
                .boardType(post.getBoard().getType())
                .writerInfo(writerInfo)
                .title(post.getTitle())
                .content(post.getContent())
                .isAnonymous(post.getIsAnonymous())
                .isAnonymousUniv(post.getIsAnonymousUniv())
                .createdAt(post.getCreatedAt())
                .commentCount(includeCommentCount ? commentCount : 0)
                .imageUrls(post.getImages().stream().map(UuidFile::getFileUrl).collect(Collectors.toList()))
                .build();
    }
}
