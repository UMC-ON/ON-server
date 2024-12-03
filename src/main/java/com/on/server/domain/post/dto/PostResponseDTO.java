package com.on.server.domain.post.dto;

import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    // 게시판 타입
    private BoardType boardType;

    // 게시글 ID
    private Long postId;

    // 작성자 정보
    private WriterInfo writerInfo;

    // 익명 여부
    private boolean isAnonymous;

    // 파견교 공개 여부
    private boolean isAnonymousUniv;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 이미지 ID 리스트
    private List<String> imageUrls;

    // 작성 시간
    private LocalDateTime createdAt;

    // 댓글 개수
    private Integer commentCount;


    public static PostResponseDTO from(Post post, boolean includeCommentCount) {
        User user = post.getUser();
        Set<UserStatus> roles = user.getRoles();
        UserStatus userStatus = roles.iterator().next();

        int commentCount = (post.getComments() != null) ? post.getComments().size() : 0;

        WriterInfo writerInfo = WriterInfo.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .country(user.getCountry())
                .dispatchedUniversity(user.getDispatchedUniversity())
                .userStatus(userStatus)
                .build();

        LocalDateTime createdAtInSeoul = post.getCreatedAt()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();

        return PostResponseDTO.builder()
                .postId(post.getId())
                .boardType(post.getBoard().getType())
                .writerInfo(writerInfo)
                .title(post.getTitle())
                .content(post.getContent())
                .isAnonymous(post.getIsAnonymous())
                .isAnonymousUniv(post.getIsAnonymousUniv())
                .createdAt(createdAtInSeoul)
                .commentCount(includeCommentCount ? commentCount : 0)
                .imageUrls(post.getImages().stream().map(UuidFile::getFileUrl).collect(Collectors.toList()))
                .build();
    }
}