package com.on.server.domain.country.domain;

import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "city")
public class City extends BaseEntity {
    @Column(name = "city_code", nullable = false)
    private String code;

    @Column(name = "city_name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
}
