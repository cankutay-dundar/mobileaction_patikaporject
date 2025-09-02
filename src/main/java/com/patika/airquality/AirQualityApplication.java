package com.patika.airquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.patika.airquality.domain")
@EnableJpaRepositories(basePackages = "com.patika.airquality.repository")
public class AirQualityApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AirQualityApplication.class, args);
    }
}
