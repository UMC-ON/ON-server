package com.on.server.domain.country.application;

import com.on.server.domain.country.domain.Country;
import com.on.server.domain.country.domain.repository.CountryRepository;
import com.on.server.domain.country.dto.CountryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<CountryDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(CountryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
