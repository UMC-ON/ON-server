package com.on.server.domain.marketPost.domain;

import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
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
    private DealStatus dealStatus = DealStatus.AWAIT;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "current_country", nullable = false)
    private String currentCountry;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @OneToMany(mappedBy = "marketPost", cascade = CascadeType.ALL)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "market_post_id")
    private List<UuidFile> images = new ArrayList<>(); // 이미지 리스트 필드 추가

    public void completeDeal() {
        if (this.dealStatus != DealStatus.AWAIT) {
            throw new IllegalStateException("거래 상태가 AWAIT인 경우에만 COMPLETE로 업데이트할 수 있습니다.");
        }
        this.dealStatus = DealStatus.COMPLETE;
    }
}




