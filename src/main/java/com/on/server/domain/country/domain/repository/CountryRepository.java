package com.on.server.domain.country.domain.repository;

import com.on.server.domain.country.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository  extends JpaRepository<Country, String> {
}
