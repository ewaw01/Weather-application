package com.example.weather_application.Errors;

public class LocationIsAlreadyIncludedException extends RuntimeException {
    public LocationIsAlreadyIncludedException(String message) {
        super(message);
    }
}
