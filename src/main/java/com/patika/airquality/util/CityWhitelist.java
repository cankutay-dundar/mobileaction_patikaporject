package com.patika.airquality.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CityWhitelist
{

    private final Set<String> allowed;

    @Autowired
    public CityWhitelist(Environment env)
    {
        String raw = env.getProperty("app.constraints.allowedCities",
                "London,Barcelona,Ankara,Tokyo,Mumbai");
        this.allowed = Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public CityWhitelist(List<String> cities)
    {
        this.allowed = (cities == null ? List.<String>of() : cities).stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public CityWhitelist(String... cities)
    {
        this.allowed = Arrays.stream(cities == null ? new String[]{} : cities)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    public boolean isAllowed(String city)
    {
        if (city == null) return false;
        return allowed.contains(city.trim().toLowerCase());
    }

    public Set<String> getAllowed() { return allowed; }
}
