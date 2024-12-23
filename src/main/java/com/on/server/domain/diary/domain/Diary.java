package com.on.server.domain.diary.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "d_day")
    private Long dDay;

    @Column(name = "diary_date", nullable = false)
    private LocalDate diaryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Diary(String content, Long dDay, LocalDate diaryDate, User user) {
        this.content = content;
        this.dDay = dDay;
        this.diaryDate = diaryDate;
        this.user = user;
    }

}
