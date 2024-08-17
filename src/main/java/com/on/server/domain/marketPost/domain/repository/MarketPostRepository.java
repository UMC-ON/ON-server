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
}