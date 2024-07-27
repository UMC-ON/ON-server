package com.on.server.domain.user.domain;

import com.on.server.domain.country.Country;
import com.on.server.domain.university.University;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "is_dispatch_confirmed", nullable = false)
    private Boolean isDispatchConfirmed;

    @Column(name = "dispatched_university")
    private String dispatchedUniversity;

    @Enumerated(EnumType.STRING)
    @Column(name = "dispatched_type")
    private DispatchedType dispatchedType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus userStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "university_id")
    private University university;

}
