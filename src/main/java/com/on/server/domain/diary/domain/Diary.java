package com.on.server.domain.diary.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    private String content;

    @Column(name = "written_date", nullable = false)
    private LocalDate writtenDate;

    private Long dDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Diary(String content, LocalDate writtenDate, Long dDay, User user) {
        this.content = content;
        this.writtenDate = writtenDate;
        this.dDay = dDay;
        this.user = user;
    }

}
