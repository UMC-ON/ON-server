package com.on.server.domain.marketPost.domain.repository;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketPostRepository extends JpaRepository<MarketPost, Long> {

    List<MarketPost> findByUser(User user);
}