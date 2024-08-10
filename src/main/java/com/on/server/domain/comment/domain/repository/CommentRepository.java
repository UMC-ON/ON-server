package com.on.server.domain.comment.domain.repository;

import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
