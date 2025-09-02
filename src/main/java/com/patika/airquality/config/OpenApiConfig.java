package com.patika.airquality.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
info = @Info(title = "Patika Air Quality API", version = "v1", description = "Historical air pollution query")
)
public class OpenApiConfig { }
