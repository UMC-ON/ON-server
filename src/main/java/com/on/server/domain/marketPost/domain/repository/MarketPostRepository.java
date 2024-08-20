package com.on.server.domain.marketPost.domain.repository;

import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPostRepository extends JpaRepository<MarketPost, Long> {

    List<MarketPost> findByUser(User user);

    // 필터링
    @Query("SELECT mp FROM MarketPost mp WHERE (:dealType IS NULL OR mp.dealType = :dealType) " +
            "AND (:currentCountry IS NULL OR mp.currentCountry = :currentCountry) " +
            "AND (:dealStatus IS NULL OR mp.dealStatus = :dealStatus)")
    List<MarketPost> findFilteredMarketPosts(
            @Param("dealType") DealType dealType,
            @Param("currentCountry") String currentCountry,
            @Param("dealStatus") DealStatus dealStatus);

    // 검색: 국가와 물품에 대한 제목 또는 내용을 검색하는 메서드
    @Query("SELECT mp FROM MarketPost mp WHERE " +
            "mp.currentCountry LIKE %:keyword% OR " +
            "mp.title LIKE %:keyword% OR " +
            "mp.content LIKE %:keyword%")
    List<MarketPost> searchMarketPosts(@Param("keyword") String keyword);

    // 내 주변 물품거래글: 특정 국가에서 거래 상태가 AWAIT인 최신순 3개의 게시글 조회
    @Query("SELECT mp FROM MarketPost mp WHERE mp.currentCountry = :currentCountry " +
            "AND mp.dealStatus = com.on.server.domain.marketPost.domain.DealStatus.AWAIT " +
            "AND mp.id <> :marketPostId " +
            "ORDER BY mp.createdAt DESC")
    List<MarketPost> findTop3ByCurrentCountryAndAwaitingOrder(@Param("currentCountry") String currentCountry, @Param("marketPostId") Long marketPostId);
}