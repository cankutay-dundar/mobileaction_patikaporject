package com.patika.airquality.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{
private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.patika.airquality.controller..*(..))")
    public void beforeController(JoinPoint jp)
    {
    log.info("HTTP → {}", jp.getSignature());
    }

    @AfterReturning("execution(* com.patika.airquality.service.AirQualityService.query(..))")
    public void afterService(JoinPoint jp)
    {
    log.info("Service completed: {}", jp.getSignature());
    }
}
