package com.on.server.domain.comment.dto;

import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.comment.domain.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    // 댓글 ID
    private Long commentId;

    // 답글 ID
    private Long replyId;

    // 게시글 ID
    private Long postId;

    // 작성자 정보
    private WriterInfo writerInfo;

    // 익명 여부
    private boolean isAnonymous;

    // 댓글 내용
    private String contents;

    // 답글 개수
    private Integer replyCount;

    // 댓글 인덱스
    private Long commentIndex;


    public static CommentResponseDTO from(Comment comment, CommentRepository commentRepository) {
        boolean isReply = comment.getParentComment() != null;

        Long replyId = null;

        if (isReply) {
            Page<Comment> repliesPage = commentRepository.findByParentComment(comment.getParentComment(), Pageable.unpaged());
            List<Comment> replies = repliesPage.getContent();
            replyId = (long) (replies.indexOf(comment) + 1);
        }

        String nickname = comment.getIsAnonymous() ? "익명" + comment.getAnonymousIndex() : comment.getUser().getNickname();

        // 댓글 인덱스 계산 (답글이 아닌 경우만)
        Long commentIndex = isReply ? null :
                commentRepository.countByPostAndCreatedAtLessThanEqual(
                        comment.getPost(),
                        comment.getCreatedAt()
                ) - 1;

        WriterInfo writerInfo = WriterInfo.builder()
                .id(comment.getUser().getId())
                .nickname(nickname)
                .build();

        return CommentResponseDTO.builder()
                .commentId(isReply ? comment.getParentComment().getId() : comment.getId())
                .replyId(replyId)
                .postId(comment.getPost().getId())
                .writerInfo(writerInfo)
                .isAnonymous(comment.getIsAnonymous())
                .contents(comment.getContents())
                .replyCount(isReply ? 0 : commentRepository.countByParentComment(comment))
             //   .replyCount(comment.getChildrenComment() != null ? comment.getChildrenComment().size() : 0)
                .commentIndex(commentIndex) // 댓글 인덱스
                .build();
    }

}
