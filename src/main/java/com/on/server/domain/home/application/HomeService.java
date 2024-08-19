package com.on.server.domain.home.application;

import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.home.dto.FreeBoardListResponseDto;
import com.on.server.domain.home.dto.InfoBoardListResponseDto;
import com.on.server.domain.post.application.PostService;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<InfoBoardListResponseDto> getInfoBoardList(User user) {
        List<Post> postList = postRepository.findTop2ByBoardOrderByCreatedAtDesc(boardRepository.findByType(BoardType.INFO));

        List<InfoBoardListResponseDto> infoBoardList = postList.stream()
                .map(post -> {
                    String writer = post.getIsAnonymous()
                            ? "익명"
                            : userRepository.findById(post.getUser().getId())
                            .orElse(null)
                            .getNickname();

                    return InfoBoardListResponseDto.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .postTime(formatTime(post.getCreatedAt()))
                            .postImg(post.getImages().isEmpty() ? null : post.getImages().get(0).getFileUrl())
                            .writer(writer)
                            .commentCount(post.getCommentCount())
                            .build();
                })
                .collect(Collectors.toList());

        return infoBoardList;
    }

    public static String formatTime(LocalDateTime createdAt) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return createdAt.format(timeFormatter);
    }

    public List<FreeBoardListResponseDto> getFreeBoardList(User user) {
        List<Post> postList = postRepository.findTop2ByBoardOrderByCreatedAtDesc(boardRepository.findByType(BoardType.FREE));

        List<FreeBoardListResponseDto> freeBoardList = postList.stream()
                .map(post -> {
                    String writer = post.getIsAnonymous()
                            ? "익명"
                            : userRepository.findById(post.getUser().getId())
                            .orElse(null)
                            .getNickname();

                    return FreeBoardListResponseDto.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .postTime(formatTime(post.getCreatedAt()))
                            .writer(writer)
                            .commentCount(post.getCommentCount())
                            .build();
                })
                .collect(Collectors.toList());

        return freeBoardList;
    }

//    public Object getCompanyBoardList(User user) {
//    }
}
