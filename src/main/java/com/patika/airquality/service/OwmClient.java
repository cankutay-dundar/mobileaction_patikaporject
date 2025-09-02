package com.patika.airquality.service;

import com.patika.airquality.dto.external.OwmGeocodeResponse;
import com.patika.airquality.dto.external.OwmPollutionHistoryResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class OwmClient
{
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public OwmClient(RestTemplate restTemplate, Environment env)
    {
        this.restTemplate = restTemplate;
        this.baseUrl = Optional.ofNullable(env.getProperty("app.owm.baseUrl"))
                .orElse("https://api.openweathermap.org");
        this.apiKey = Optional.ofNullable(env.getProperty("app.owm.apiKey"))
                .orElseGet(() -> System.getenv("OWM_API_KEY"));
        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new IllegalStateException("OWM API key is missing. Set OWM_API_KEY env or app.owm.apiKey.");
        }
    }

    public OwmGeocodeResponse[] geocode(String city)
    {
        String url = "%s/geo/1.0/direct?q=%s&limit=1&appid=%s"
                .formatted(baseUrl, enc(city), apiKey);
        return restTemplate.getForObject(URI.create(url), OwmGeocodeResponse[].class);
    }

    public OwmPollutionHistoryResponse pollutionHistory(double lat, double lon, long startUnix, long endUnix)
    {
        String url = "%s/data/2.5/air_pollution/history?lat=%s&lon=%s&start=%d&end=%d&appid=%s"
                .formatted(baseUrl, lat, lon, startUnix, endUnix, apiKey);
        return restTemplate.getForObject(URI.create(url), OwmPollutionHistoryResponse.class);
    }

    private String enc(String s)
    {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
