package com.patika.airquality.dto.request;

import java.time.LocalDate;

public record AirQualityQueryRequest(
String city,
LocalDate startDate,
LocalDate endDate
) { }
