package com.on.server.domain.scrap.domain;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "scrap")
public class Scrap extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_post_id", nullable = false)
    private MarketPost marketPost;
}
