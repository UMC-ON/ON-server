package com.on.server.domain.scrap.domain.repository;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    List<Scrap> findByUser(User user);

    Optional<Scrap> findByUserAndMarketPost(User user, MarketPost marketPost);
}
