package com.on.server.domain.home.application;

import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.board.domain.repository.BoardRepository;
import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.companyPost.domain.repository.CompanyPostRepository;
import com.on.server.domain.home.dto.CompanyBoardListResponseDto;
import com.on.server.domain.home.dto.FreeBoardListResponseDto;
import com.on.server.domain.home.dto.InfoBoardListResponseDto;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.util.FormatTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CompanyPostRepository companyPostRepository;

    public List<InfoBoardListResponseDto> getInfoBoardList(User user) {
        List<Post> postList = postRepository.findTop2ByBoardOrderByCreatedAtDesc(boardRepository.findByType(BoardType.INFO));

        List<InfoBoardListResponseDto> infoBoardList = postList.stream()
                .map(post -> {
                    String writer = post.getIsAnonymous()
                            ? "익명"
                            : userRepository.findById(post.getUser().getId())
                            .orElse(null)
                            .getNickname();

                    int commentCount = post.getComments() != null ? post.getComments().size() : 0;

                    return InfoBoardListResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .postTime(FormatTimeUtil.localDateTime2FormattedString(post.getCreatedAt()))
                            .postImg(post.getImages().isEmpty() ? null : post.getImages().get(0).getFileUrl())
                            .writer(writer)
                            .commentCount(commentCount)
                            .build();
                })
                .collect(Collectors.toList());

        return infoBoardList;
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

                    int commentCount = post.getComments() != null ? post.getComments().size() : 0;

                    return FreeBoardListResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .postTime(FormatTimeUtil.localDateTime2FormattedString(post.getCreatedAt()))
                            .writer(writer)
                            .commentCount(commentCount)
                            .build();
                })
                .collect(Collectors.toList());

        return freeBoardList;
    }

    // 내 주변 동행글
    public List<CompanyBoardListResponseDto> getCompanyBoardList(User user) {

        String country = user.getCountry();

        List<CompanyPost> companyPostList = companyPostRepository.findTop5ByTravelArea(country, PageRequest.of(0, 5));


        if (companyPostList.isEmpty()) {
            return null;
        }

        return companyPostList.stream()
                .map(CompanyBoardListResponseDto::getCompanyBoardDto)
                .toList();
    }

}
