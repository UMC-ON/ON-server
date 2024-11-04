package com.on.server.domain.country.domain;

import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "country")
public class Country  extends BaseEntity {

    @Column(name = "country_code", nullable = false)
    private String code;

    @Column(name = "country_name", nullable = false)
    private String name;

    @Column(name = "country_flag", nullable = false)
    private String flag;

    @Column(name = "continent", nullable = false)
    private String continent;
}
