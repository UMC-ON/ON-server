package com.on.server.domain.companyPost.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
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

    @Column(name = "departure_place", nullable = false)
    private String departurePlace;

    @Column(name = "arrive_place", nullable = false)
    private String arrivePlace;

    @Column(name = "recruit_number", nullable = false)
    private Long recruitNumber;

    @Column(name = "schedule_pariod_day", nullable = false)
    private Long schedulePeriodDay;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

//    @OneToMany(mappedBy = "companyPost", cascade = CascadeType.ALL)
//    private List<Image> images = new ArrayList<>();
//
//    @OneToMany(mappedBy = "companyPost", cascade = CascadeType.ALL)
//    private List<CompanyParticipant> participants = new ArrayList<>();

}
