package com.on.server.domain.country.dto;

import com.on.server.domain.country.domain.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private String code;
    private String name;

    public static CityDTO fromEntity(City city) {
        return CityDTO.builder()
                .code(city.getCode())
                .name(city.getName())
                .build();
    }
}
