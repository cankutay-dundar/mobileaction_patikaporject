package com.patika.airquality.dto.response;

import java.time.LocalDate;
import java.util.Map;

public record AirQualityDailyCategoryDto(
LocalDate date,
Map<String, String> categories
) { }
