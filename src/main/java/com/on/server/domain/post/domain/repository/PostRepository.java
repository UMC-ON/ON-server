package com.on.server.domain.post.domain.repository;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 게시판의 모든 게시글 조회
    Page<Post> findByBoardOrderByCreatedAtDesc(Board board, Pageable pageable);

    // 특정 사용자가 게시판에 작성한 모든 게시글 조회
    Page<Post> findByUserAndBoardOrderByCreatedAtDesc(User user, Board board, Pageable pageable);

    // 제목 또는 내용에 키워드가 포함된 게시글 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    // 작성자의 국가를 기준으로 게시글 필터링
    @Query("SELECT p FROM Post p WHERE p.board = :board AND p.user.country = :country ORDER BY p.createdAt DESC")
    List<Post> findByBoardAndUserCountry(@Param("board") Board board, @Param("country") String country);


    // 특정 게시판에서 최신 게시글 4개 조회
    List<Post> findTop4ByBoardOrderByCreatedAtDesc(Board board);

    List<Post> findTop2ByBoardOrderByCreatedAtDesc(Optional<Board> byType);
}
