package com.on.server.domain.country;

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
public class Country extends BaseEntity {

    @Column(name = "name")
    private String name;

}
