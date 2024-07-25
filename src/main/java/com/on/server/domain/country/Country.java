package com.on.server.domain.country;

import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "country")
public class Country extends BaseEntity {

    @Column(name = "name")
    private String name;

}
