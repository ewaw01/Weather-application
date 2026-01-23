package com.example.weather_application.Controllers;

import com.example.weather_application.User.User;
import com.example.weather_application.Services.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final Logger log = LoggerFactory.getLogger(WeatherController.class);

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping
    public ResponseEntity<Void> postUser (
            @RequestBody User user
    ) {
        log.info("Posting user with id {}", user.id());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(weatherService.addUser(user));
    }

}
