package com.on.server.domain.post.domain.repository;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자와 게시판의 모든 게시글 조회
    List<Post> findByUserAndBoard(User user, Board board);

    // 제목 또는 내용에 키워드가 포함된 게시글 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> searchPosts(@Param("keyword") String keyword);

    // 제목 또는 내용에 국가 이름이 포함된 게시글 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:country% OR p.content LIKE %:country%")
    List<Post> findByCountryInTitleOrContent(@Param("country") String country);

    // 특정 게시판에서 최신 게시글 4개 조회
    List<Post> findTop4ByBoardOrderByCreatedAtDesc(Board board);
}
