package com.example.weather_application.Location;

public record LocationSearchFilter(
        Long id,
        String name,
        String country,
        String description,
        String icon,
        Double temperature,
        Long humidity, //in percent
        String windSpeed,
        Long sunrise,
        Long sunset,
        String time,
        Integer pageNum,
        Integer pageSize
) {
}
