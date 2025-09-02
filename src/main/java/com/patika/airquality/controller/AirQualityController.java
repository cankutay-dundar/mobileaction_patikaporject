package com.patika.airquality.controller;

import com.patika.airquality.dto.request.AirQualityQueryRequest;
import com.patika.airquality.dto.response.AirQualityQueryResponse;
import com.patika.airquality.service.AirQualityService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/air-quality")
public class AirQualityController
{

private final AirQualityService service;

    public AirQualityController(AirQualityService service)
    {
    this.service = service;
    }

    @GetMapping
    public AirQualityQueryResponse query(
    @RequestParam @NotBlank String city,
    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
    @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate)
    {
    AirQualityQueryRequest req = new AirQualityQueryRequest(city, startDate, endDate);
    return service.query(req);
    }
}
