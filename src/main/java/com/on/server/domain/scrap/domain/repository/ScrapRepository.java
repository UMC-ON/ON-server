package com.on.server.domain.scrap.domain.repository;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @Query("SELECT s FROM Scrap s WHERE s.user = :user ORDER BY s.createdAt DESC")
    Page<Scrap> findByUser(@Param("user") User user, Pageable pageable);

    Optional<Scrap> findByUserAndMarketPost(User user, MarketPost marketPost);
}