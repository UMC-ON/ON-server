package com.on.server.domain.university;

import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "university")
public class University extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "homepage_url")
    private String homepageUrl;
}
