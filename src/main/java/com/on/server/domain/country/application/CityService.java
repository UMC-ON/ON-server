package com.on.server.domain.country.application;

import com.on.server.domain.country.domain.City;
import com.on.server.domain.country.domain.repository.CityRepository;
import com.on.server.domain.country.dto.CityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDTO> getCitiesByCountry(String countryName) {
        List<City> cities = cityRepository.findByCountryName(countryName);
        return cities.stream()
                .map(CityDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
