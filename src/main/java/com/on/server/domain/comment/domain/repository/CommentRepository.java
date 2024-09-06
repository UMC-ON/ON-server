package com.on.server.domain.comment.domain.repository;

import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.user.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글의 모든 댓글 및 답글 조회
    Page<Comment> findByPost(Post post, Pageable pageable);

    // 댓글에 대한 답글 조회
    Page<Comment> findByParentComment(Comment parentComment, Pageable pageable);

    // 사용자가 게시글에 작성한 익명 댓글 조회
    List<Comment> findByUserAndPostAndIsAnonymousTrue(User user, Post post);

    // 해당 게시글에 작성된 모든 익명 댓글의 익명 인덱스를 조회
    @Query("SELECT c.anonymousIndex FROM Comment c WHERE c.post = :post AND c.anonymousIndex IS NOT NULL")
    List<Integer> findAnonymousIndicesByPost(@Param("post") Post post);
}
