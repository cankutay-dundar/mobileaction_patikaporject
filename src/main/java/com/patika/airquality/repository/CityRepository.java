package com.patika.airquality.repository;

import com.patika.airquality.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long>
{
    Optional<City> findByNameIgnoreCase(String name);
}