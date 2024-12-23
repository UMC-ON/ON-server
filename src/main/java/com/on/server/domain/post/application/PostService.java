package com.on.server.domain.post.application;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UuidFileService uuidFileService;

    // 특정 게시판에 새로운 게시글 작성
    @Transactional
    public PostResponseDTO createPost(BoardType boardType, PostRequestDTO requestDTO, List<MultipartFile> imageFiles, User user) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시판을 찾을 수 없습니다."));

        List<UuidFile> uploadedImages = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            uploadedImages = imageFiles.stream()
                    .map(file -> uuidFileService.saveFile(file, FilePath.POST))
                    .collect(Collectors.toList());
        }

        Post post = Post.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.isAnonymous())
                .isAnonymousUniv(requestDTO.isAnonymousUniv())
                .board(board)
                .images(uploadedImages)
                .build();

        post = postRepository.save(post);

        return PostResponseDTO.from(post, true);
    }

    // 특정 게시글 조회
    public PostResponseDTO getPostById(BoardType boardType, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + postId));
        if (!post.getBoard().getType().equals(boardType)) {
            throw new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 게시판에 게시글이 존재하지 않습니다.");
        }
        return PostResponseDTO.from(post, true);
    }


    // 특정 게시글 삭제
    @Transactional
    public void deletePost(User user, BoardType boardType, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다."));

        if (!post.getBoard().getType().equals(boardType)) {
            throw new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당 게시판에 게시글이 존재하지 않습니다.");
        }

        if (!post.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException(ResponseCode.INVALID_REQUEST_ROLE, "삭제 권한이 없습니다.");
        }

        List<UuidFile> images = post.getImages();
        if (images != null) {
            for (UuidFile image : images) {
                uuidFileService.deleteFile(image);
            }
        }

        postRepository.delete(post);
    }

    // 게시글 검색 기능
    public Page<PostResponseDTO> searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(keyword, pageable);
        return posts.map(post -> PostResponseDTO.from(post, true));
    }

    // 국가 필터링 메서드
    public Page<PostResponseDTO> getPostsByCountryAndBoardType(BoardType boardType, String country,  Pageable pageable) {
        Board board = boardRepository.findByType(boardType)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시판을 찾을 수 없습니다."));
        Page<Post> posts = postRepository.findByBoardAndUserCountry(board, country, pageable);

        return posts.map(post -> PostResponseDTO.from(post, true));
    }
}
