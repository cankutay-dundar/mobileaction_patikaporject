package com.patika.airquality.service;

import com.patika.airquality.domain.City;
import com.patika.airquality.domain.DataSourceType;
import com.patika.airquality.domain.PollutionSample;
import com.patika.airquality.dto.external.OwmGeocodeResponse;
import com.patika.airquality.dto.external.OwmPollutionHistoryResponse;
import com.patika.airquality.dto.request.AirQualityQueryRequest;
import com.patika.airquality.dto.response.AirQualityDailyCategoryDto;
import com.patika.airquality.dto.response.AirQualityQueryResponse;
import com.patika.airquality.exception.ApiException;
import com.patika.airquality.exception.InvalidDateRangeException;
import com.patika.airquality.mapper.AirQualityMapper;
import com.patika.airquality.repository.CityRepository;
import com.patika.airquality.repository.PollutionSampleRepository;
import com.patika.airquality.util.CityWhitelist;
import com.patika.airquality.util.DateUtils;
import com.patika.airquality.util.RangeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AirQualityService
{
    private static final Logger log = LoggerFactory.getLogger(AirQualityService.class);

    private final PollutionSampleRepository pollutionRepo;
    private final CityRepository cityRepo;
    private final OwmClient owmClient;
    private final ClassificationService classify;
    private final DateUtils dates;
    private final CityWhitelist whitelist;

    public AirQualityService(PollutionSampleRepository pollutionRepo,
                             CityRepository cityRepo,
                             OwmClient owmClient,
                             ClassificationService classify,
                             DateUtils dates,
                             CityWhitelist whitelist)
    {
        this.pollutionRepo = pollutionRepo;
        this.cityRepo = cityRepo;
        this.owmClient = owmClient;
        this.classify = classify;
        this.dates = dates;
        this.whitelist = whitelist;
    }

    @Transactional
    public AirQualityQueryResponse query(AirQualityQueryRequest req)
    {
        String city = req.city();
        if (!whitelist.isAllowed(city)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "City not allowed: " + city);
        }

        LocalDate start = (req.startDate() == null) ? dates.defaultStart() : req.startDate();
        LocalDate end   = (req.endDate()   == null) ? dates.defaultEnd()   : req.endDate();

        try {
            dates.ensureWithinBounds(start, end);
        } catch (IllegalArgumentException e) {
            throw new InvalidDateRangeException(e.getMessage());
        }

        List<PollutionSample> existing =
                pollutionRepo.findByCityNameIgnoreCaseAndDateBetweenOrderByDateAsc(city, start, end);
        Set<LocalDate> have = existing.stream().map(PollutionSample::getDate).collect(Collectors.toSet());

        List<LocalDate> allDays = RangeUtils.enumerate(start, end);
        List<LocalDate> missing = allDays.stream().filter(d -> !have.contains(d)).toList();

        if (!missing.isEmpty()) {
            City ci = cityRepo.findByNameIgnoreCase(city).orElseGet(() -> geocodeAndSave(city));

            long startUnix = dates.toUnixStartOfDay(start);
            long endUnix = dates.toUnixEndOfDay(end);

            OwmPollutionHistoryResponse resp =
                    owmClient.pollutionHistory(ci.getLat(), ci.getLon(), startUnix, endUnix);

            if (resp != null && resp.getList() != null) {
                Map<LocalDate, OwmPollutionHistoryResponse.Components> byDay = new HashMap<>();
                for (OwmPollutionHistoryResponse.Item it : resp.getList()) {
                    LocalDate day = Instant.ofEpochSecond(it.getDt()).atZone(ZoneOffset.UTC).toLocalDate();
                    if (day.isBefore(start) || day.isAfter(end)) continue;
                    byDay.put(day, it.getComponents());
                }
                for (LocalDate d : missing) {
                    OwmPollutionHistoryResponse.Components c = byDay.get(d);
                    if (c == null) continue;
                    PollutionSample s = new PollutionSample(
                            city, d, c.getCo(), c.getSo2(), c.getO3(), DataSourceType.API
                    );
                    pollutionRepo.save(s);
                    log.info("Saved API data: city={}, date={}", city, d);
                }
            }
        }

        List<PollutionSample> all =
                pollutionRepo.findByCityNameIgnoreCaseAndDateBetweenOrderByDateAsc(city, start, end);

        List<AirQualityDailyCategoryDto> out = new ArrayList<>();
        for (PollutionSample s : all) {
            Map<String, String> cats = new LinkedHashMap<>();
            cats.put("CO", classify.classify("CO", safe(s.getCo())));
            cats.put("SO2", classify.classify("SO2", safe(s.getSo2())));
            cats.put("O3", classify.classify("O3", safe(s.getO3())));
            out.add(AirQualityMapper.dailyDto(s, cats));
            log.info("Source for {} {} → {}", s.getCityName(), s.getDate(), s.getSource());
        }

        return new AirQualityQueryResponse(capitalize(city), out);
    }

    private City geocodeAndSave(String city)
    {
        OwmGeocodeResponse[] arr = owmClient.geocode(city);
        if (arr == null || arr.length == 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Could not geocode city: " + city);
        }
        OwmGeocodeResponse g = arr[0];
        City c = new City(capitalize(city), g.getLat(), g.getLon());
        return cityRepo.save(c);
    }

    private String capitalize(String s)
    {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private double safe(Double v)
    {
        return v == null ? 0.0 : v;
    }
}
