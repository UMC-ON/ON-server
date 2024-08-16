package com.on.server.domain.marketPost.domain;

import com.on.server.domain.country.Country;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "market_post")
public class MarketPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_share", nullable = false)
    private boolean isShare;

    @Column(name = "cost", nullable = false)
    private Long cost;

    @Column(name = "deal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Column(name = "deal_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    @Column(name = "content", nullable = false)
    private String content;

//    @OneToMany(mappedBy = "market_post", cascade = CascadeType.ALL)
//    private List<Image> images = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

//    @OneToOne
//    @JoinColumn(name = "location_id", nullable = false)
//    private Location location;
//
    @OneToMany(mappedBy = "market_post", cascade = CascadeType.ALL)
    private List<Scrap> scraps = new ArrayList<>();

    public enum DealType {
        DIRECT, DELIVERY
    }

    public enum DealStatus {
        COMPLETE, AWAIT
    }
}




