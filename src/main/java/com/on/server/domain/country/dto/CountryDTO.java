package com.on.server.domain.country.dto;

import com.on.server.domain.country.domain.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {
    private String code;
    private String name;
    private String flag;
    private String continent;

    public static CountryDTO fromEntity(Country country) {
        return CountryDTO.builder()
                .code(country.getCode())
                .name(country.getName())
                .flag(country.getFlag())
                .continent(country.getContinent())
                .build();
    }
}
