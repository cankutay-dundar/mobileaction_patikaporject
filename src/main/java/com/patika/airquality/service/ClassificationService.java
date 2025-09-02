package com.patika.airquality.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationService
{
    @Value("${app.caqi.thresholds.co:1,2,10,17,34}")
    private List<Double> coBreaks = List.of(1d, 2d, 10d, 17d, 34d);

    @Value("${app.caqi.thresholds.so2:40,80,380,800,1600}")
    private List<Double> so2Breaks = List.of(40d, 80d, 380d, 800d, 1600d);

    @Value("${app.caqi.thresholds.o3:50,100,168,208,748}")
    private List<Double> o3Breaks = List.of(50d, 100d, 168d, 208d, 748d);

    private static final String[] CATS = new String[]
    {
            "Good", "Satisfactory", "Moderate", "Poor", "Severe", "Hazardous"
    };

    public ClassificationService() {}

    public ClassificationService(List<Double> co, List<Double> so2, List<Double> o3)
    {
        this.coBreaks = co;
        this.so2Breaks = so2;
        this.o3Breaks = o3;
    }

    public String classify(String pollutant, double value)
    {
        List<Double> b;
        double adjusted;

        switch (pollutant.toUpperCase()) {
            case "CO":
                b = coBreaks;
                adjusted = value / 1000.0;
                break;
            case "SO2":
                b = so2Breaks;
                adjusted = value * 100.0;
                break;
            case "O3":
                b = o3Breaks;
                adjusted = value;
                break;
            default:
                throw new IllegalArgumentException("Unsupported pollutant: " + pollutant);
        }

        return bucket(b, adjusted);
    }

    private String bucket(List<Double> breaks, double v)
    {
        if (v <= breaks.get(0)) return CATS[0];
        if (v <= breaks.get(1)) return CATS[1];
        if (v <= breaks.get(2)) return CATS[2];
        if (v <= breaks.get(3)) return CATS[3];
        if (v <= breaks.get(4)) return CATS[4];
        return CATS[5];
    }
}
