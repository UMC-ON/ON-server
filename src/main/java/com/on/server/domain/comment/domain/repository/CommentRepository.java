package com.on.server.domain.comment.domain.repository;

import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);           // 게시글의 모든 댓글 및 답글 조회
    List<Comment> findByParentComment(Comment parentComment); // 부모 댓글의 답글 조회
}
