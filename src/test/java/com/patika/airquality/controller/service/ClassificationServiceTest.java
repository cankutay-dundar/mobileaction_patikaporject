package com.patika.airquality.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassificationService – unit-aware tests")
class ClassificationServiceTest 
{
    private final ClassificationService svc = new ClassificationService(
            List.of(1d, 2d, 10d, 17d, 34d),
            List.of(40d, 80d, 380d, 800d, 1600d),
            List.of(50d, 100d, 168d, 208d, 748d)
    );

    @Nested
    @DisplayName("CO input is in µg/m³ (service divides by 1000)")
    class COTests
    {

        @ParameterizedTest(name = "CO={0} µg/m³ => {1}")
        @CsvSource({
                "500, Good",
                "1000, Good",
                "1500, Satisfactory",
                "2000, Satisfactory",
                "5000, Moderate",
                "10000, Moderate",
                "12000, Poor",
                "17000, Poor",
                "20000, Severe",
                "34000, Severe",
                "40000, Hazardous"
        })
        void should_classify_CO(double valueUg, String expected)
        {
            assertEquals(expected, svc.classify("CO", valueUg));
        }
    }

    @Nested
    @DisplayName("SO₂ input is scaled by ×100 inside service")
    class SO2Tests
    {
        @ParameterizedTest(name = "SO2={0} (×100 ⇒ {0}×100 µg/m³) => {1}")
        @CsvSource(
        {
                "0.10, Good",
                "0.40, Good",
                "0.60, Satisfactory",
                "0.80, Satisfactory",
                "2.0,  Moderate",
                "3.8,  Moderate",
                "6.0,  Poor",
                "8.0,  Poor",
                "12.0, Severe",
                "16.0, Severe",
                "20.0, Hazardous"
        })
        void should_classify_SO2(double rawInput, String expected)
        {
            assertEquals(expected, svc.classify("SO2", rawInput));
        }
    }

    @Nested
    @DisplayName("O₃ input is µg/m³ (no scaling)")
    class O3Tests
    {
        @ParameterizedTest(name = "O3={0} µg/m³ => {1}")
        @CsvSource(
        {
                "30,  Good",
                "50,  Good",
                "75,  Satisfactory",
                "100, Satisfactory",
                "150, Moderate",
                "168, Moderate",
                "180, Poor",
                "208, Poor",
                "300, Severe",
                "748, Severe",
                "800, Hazardous"
        })
        void should_classify_O3(double valueUg, String expected)
        {
            assertEquals(expected, svc.classify("O3", valueUg));
        }
    }

    @Test
    @DisplayName("Unsupported pollutant throws")
    void should_throw_for_unsupported_pollutant()
    {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> svc.classify("PM10", 42));
        assertTrue(ex.getMessage().contains("Unsupported pollutant"));
    }
}
