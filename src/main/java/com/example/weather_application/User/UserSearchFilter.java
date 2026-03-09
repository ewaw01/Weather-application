package com.example.weather_application.User;

public record UserSearchFilter(
        Long id,
        String userId,
        Integer pageNum,
        Integer pageSize
) {
}
