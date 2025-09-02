package com.patika.airquality.repository;

import com.patika.airquality.domain.PollutionSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PollutionSampleRepository extends JpaRepository<PollutionSample, Long>
{
    List<PollutionSample> findByCityNameIgnoreCaseAndDateBetweenOrderByDateAsc(String cityName, LocalDate start, LocalDate end);
    boolean existsByCityNameIgnoreCaseAndDate(String cityName, LocalDate date);
}

