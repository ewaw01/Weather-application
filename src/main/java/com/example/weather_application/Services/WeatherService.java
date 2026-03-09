package com.example.weather_application.Services;

import com.example.weather_application.Location.Location;
import com.example.weather_application.Location.LocationEntity;
import com.example.weather_application.Errors.LocationIsAlreadyIncludedException;
import com.example.weather_application.Mappers.LocationMapper;
import com.example.weather_application.Repos.LocationRepository;
import com.example.weather_application.Repos.UserRepository;
import com.example.weather_application.User.UserEntity;
import com.example.weather_application.Mappers.UserMapper;
import com.example.weather_application.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class WeatherService {
    private final Logger log = LoggerFactory.getLogger(WeatherService.class);

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.base-url}")
    private String baseUrl;

    @Value("${openweather.defaults.units}")
    private String defaultUnits;

    @Value("${openweather.defaults.lang}")
    private String defaultLang;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final Utils utils;

    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherService(UserRepository userRepository, LocationRepository locationRepository, Utils utils, UserMapper userMapper, LocationMapper locationMapper) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.utils = utils;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
    }

    public LocationEntity getInfoLocation(
            String nameLocation
    ) {
        log.info("getInfoLocation call");

        String url = baseUrl + "/weather?q=" + nameLocation + "&appid=" + apiKey + "&units=" + defaultUnits + "&lang=" + defaultLang;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = response.getBody();

        if (locationRepository.findByName(nameLocation).isPresent()) {
            LocationEntity location = new LocationEntity(
                    locationRepository.findByName(nameLocation).get().getId(),
                    ((String)body.get("name")).toLowerCase(),
                    (String) ((Map<String, Object>)body.get("sys")).get("country"),
                    (String) ((Map<String, Object>)((ArrayList)body.get("weather")).get(0)).get("description"),
                    (String) ((Map<String, Object>)((ArrayList)body.get("weather")).get(0)).get("icon"),
                    (Double) ((Map<String, Object>)body.get("main")).get("temp"),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("main")).get("humidity")),
                    String.valueOf(((Map<String, Object>)body.get("wind")).get("speed")),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("sys")).get("sunrise")),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("sys")).get("sunset")),
                    utils.calculateTheTimeInterval(LocalDateTime.now()
                            .toString().split("T")[1].substring(0, 2))
            );

            return location;
        } else {
            LocationEntity newLocation = new LocationEntity(
                    null,
                    ((String)body.get("name")).toLowerCase(),
                    (String) ((Map<String, Object>)body.get("sys")).get("country"),
                    (String) ((Map<String, Object>)((ArrayList)body.get("weather")).get(0)).get("description"),
                    (String) ((Map<String, Object>)((ArrayList)body.get("weather")).get(0)).get("icon"),
                    (Double) ((Map<String, Object>)body.get("main")).get("temp"),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("main")).get("humidity")),
                    String.valueOf(((Map<String, Object>)body.get("wind")).get("speed")),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("sys")).get("sunrise")),
                    Long.valueOf((Integer)((Map<String, Object>)body.get("sys")).get("sunset")),
                    utils.calculateTheTimeInterval(LocalDateTime.now()
                            .toString().split("T")[1].substring(0, 2))
            );

            return newLocation;
        }
    }

    public Location postLocationForUser(
            String nameLocation,
            Long userId
    ) {
        log.info("postLocationForUser call");

        if(userRepository.findById(userId).get().getLocations().contains(nameLocation)) {
            throw new LocationIsAlreadyIncludedException("Location already exists");
        }

        UserEntity oldUser = userRepository.findById(userId).orElseThrow(()->
                new NoSuchElementException("User Not Found"));

//        userRepository.save(new UserEntity(
//                oldUser.getId(),
//                oldUser.getUserId(),
//                oldUser.getLocations() + " " + nameLocation + " "
//        ));

        if(locationRepository.findByName(nameLocation).isPresent()) {
            userRepository.save(new UserEntity(
                    oldUser.getId(),
                    oldUser.getUserId(),
                    oldUser.getLocations() + " " + nameLocation + " "
            ));

            LocationEntity newLocation = locationRepository.findByName(nameLocation).get();

            return locationMapper.toDomain(newLocation);
        } else {
            LocationEntity newLocation = getInfoLocation(nameLocation);

            userRepository.save(new UserEntity(
                    oldUser.getId(),
                    oldUser.getUserId(),
                    oldUser.getLocations() + " " + nameLocation + " "
            ));

            locationRepository.save(newLocation);

            return locationMapper.toDomain(newLocation);
        }
    }

}
