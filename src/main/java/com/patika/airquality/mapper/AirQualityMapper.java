package com.patika.airquality.mapper;

import com.patika.airquality.domain.PollutionSample;
import com.patika.airquality.dto.response.AirQualityDailyCategoryDto;

import java.util.Map;

public class AirQualityMapper
{
    public static AirQualityDailyCategoryDto dailyDto(
    PollutionSample s,
    Map<String, String> categories
    ) {
    return new AirQualityDailyCategoryDto(s.getDate(), categories);
    }
}