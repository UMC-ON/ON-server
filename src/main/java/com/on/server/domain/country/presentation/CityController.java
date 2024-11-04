package com.on.server.domain.country.presentation;

import com.on.server.domain.country.application.CityService;
import com.on.server.domain.country.dto.CityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "도시")
@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @Operation(summary = "특정 국가에 대한 도시 목록 조회")
    @GetMapping("/{countryName}")
    public ResponseEntity<List<CityDTO>> getCitiesByCountry(@PathVariable("countryName") String countryName) {
        List<CityDTO> cities = cityService.getCitiesByCountry(countryName);
        return ResponseEntity.ok(cities);
    }
}
