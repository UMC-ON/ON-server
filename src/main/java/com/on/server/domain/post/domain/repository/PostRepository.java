package com.on.server.domain.post.domain.repository;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
    List<Post> findByUser(User user);
    List<Post> findByUserAndBoard(User user, Board board);
}