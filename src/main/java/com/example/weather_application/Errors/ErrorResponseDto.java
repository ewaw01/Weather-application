package com.example.weather_application.Errors;

import java.time.LocalDateTime;

public record ErrorResponseDto (
    String message,
    String detailMessage,
    LocalDateTime time
) {
}
