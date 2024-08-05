package com.on.server.domain.image.domain;


import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.post.domain.Post;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "uuid",nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ImageType type;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_post_id", nullable = false)
    private MarketPost marketPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_post_id", nullable = false)
    private CompanyPost companyPost;

}
