package com.example.weather_application.Errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error("Handled exception" + e.getMessage());
        return ResponseEntity.internalServerError().body(
                new ErrorResponseDto(
                        "Something went wrong",
                        e.getMessage(),
                        LocalDateTime.now()
                )
        );
    }

}
