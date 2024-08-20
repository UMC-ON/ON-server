package com.on.server.domain.companyPost.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_post")
public class CompanyPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "age_anonymous", nullable = false)
    private boolean ageAnonymous;

    @Column(name = "university_anonymous", nullable = false)
    private boolean universityAnonymous;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    // 여행 지역 리스트
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_post_travel_area", joinColumns = @JoinColumn(name = "company_post_id"))
    @Column(name = "travel_area")
    private List<String> travelArea = new ArrayList<>();

    @Column(name = "current_recruit_number", nullable = false)
    private Long currentRecruitNumber = 0L;

    @Column(name = "total_recruit_number", nullable = false)
    private Long totalRecruitNumber;

    @Column(name = "is_recruit_completed", nullable = false)
    private boolean isRecruitCompleted;

    @Column(name = "schedule_period_day", nullable = false)
    private Long schedulePeriodDay;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "company_post_id")
    private List<UuidFile> images = new ArrayList<>();

    public void updateCurrentNumber(Long currentRecruitNumber) {
        this.currentRecruitNumber = currentRecruitNumber;
    }

    public void updateRecruitCompleted(boolean isRecruitCompleted) {
        this.isRecruitCompleted = isRecruitCompleted;
    }

}
