package com.patika.airquality.dto.response;

import java.util.List;

public record AirQualityQueryResponse(
String City,
List<AirQualityDailyCategoryDto> Results
) { }