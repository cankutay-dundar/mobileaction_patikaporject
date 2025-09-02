package com.patika.airquality.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<?> handleInvalidDate(InvalidDateRangeException e)
    {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApi(ApiException e)
    {
    return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e)
    {
    return ResponseEntity.badRequest().body(Map.of("error", "Validation error"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegal(IllegalArgumentException e)
    {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOther(Exception e)
    {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal error"));
    }
}

