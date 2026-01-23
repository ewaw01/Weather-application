package com.example.weather_application.User;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record User (
        @Nullable
        Long id,
        @NotBlank(message = "user id is not null")
        String userId,
        @Nullable
        List<String> locations
) {
}
