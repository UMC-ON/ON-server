package com.on.server.domain.companyParticipant.domain;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_participant")
public class CompanyParticipant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_post_id", nullable = false)
    private CompanyPost companyPost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CompanyParticipantStatus companyParticipantstatus;
}