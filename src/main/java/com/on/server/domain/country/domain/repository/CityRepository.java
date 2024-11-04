package com.on.server.domain.country.domain.repository;

import com.on.server.domain.country.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository  extends JpaRepository<City, String> {
    List<City> findByCountryName(String countryName);
}
