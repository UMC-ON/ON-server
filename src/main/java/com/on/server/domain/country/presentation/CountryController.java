package com.on.server.domain.country.presentation;

import com.on.server.domain.country.application.CountryService;
import com.on.server.domain.country.dto.CountryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "국가")
@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @Operation(summary = "전체 국가 조회")
    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }
}
