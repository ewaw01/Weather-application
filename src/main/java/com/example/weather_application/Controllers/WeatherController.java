package com.example.weather_application.Controllers;

import com.example.weather_application.Location.Location;
import com.example.weather_application.Location.LocationSearchFilter;
import com.example.weather_application.Services.WeatherService;
import com.example.weather_application.User.User;
import com.example.weather_application.Services.MainService;
import com.example.weather_application.User.UserSearchFilter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final Logger log = LoggerFactory.getLogger(WeatherController.class);

    private final MainService mainService;
    private final WeatherService weatherService;

    public WeatherController(MainService mainService, WeatherService weatherService) {
        this.mainService = mainService;
        this.weatherService = weatherService;
    }

    @PostMapping("/add_user")
    public ResponseEntity<User> postUser (
            @RequestBody @Valid User user
    ) {
        log.info("Posting new user with user id " + user.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mainService.addUser(user));
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> findUserByFilter(
        @RequestParam(name = "id", required = false) Long id,
        @RequestParam(name = "userId", required = false) String userId,
        @RequestParam(name = "pageNum", required = false) Integer pageNum,
        @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        log.info("Finding users by filters");

        UserSearchFilter filter = new UserSearchFilter(
                id,
                userId,
                pageNum,
                pageSize
        );

        return ResponseEntity.ok().body(
                mainService.searchAllUsersByFilter(filter)
        );
    }

    @GetMapping("/location")
    public ResponseEntity<List<Location>> findLocationByFilter(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "icon", required = false) String icon,
            @RequestParam(name = "temperature", required = false) Double temperature,
            @RequestParam(name = "humidity", required = false) Long humidity,
            @RequestParam(name = "windSpeed", required = false) String windSpeed,
            @RequestParam(name = "sunrise", required = false) Long sunrise,
            @RequestParam(name = "sunset", required = false) Long sunset,
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        log.info("Finding locations by filters");

        LocationSearchFilter filter = new LocationSearchFilter(
                id,
                name,
                country,
                description,
                icon,
                temperature,
                humidity,
                windSpeed,
                sunrise,
                sunset,
                time,
                pageNum,
                pageSize
        );

        return ResponseEntity.ok().body(
                mainService.searchAllLocationsByFilter(filter)
        );
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<Void> deleteUser (
            @PathVariable Long id
    ) {
        log.info("Deleting user with id {}", id);
        return ResponseEntity.ok()
                .body(mainService.deleteUser(id));
    }

    @DeleteMapping("/delete_location/{id}")
    public ResponseEntity<Void> deleteLocation (
            @PathVariable Long id
    ) {
        log.info("Deleting location with id {}", id);
        return ResponseEntity.ok()
                .body(mainService.deleteLocation(id));
    }

    @PutMapping("/add_location/{userId}")
    public ResponseEntity<Location> postLocation (
           @PathVariable Long userId,
           @RequestBody Location location
    ) {
        log.info("Posting location " + location.name() + " for user with id {}", userId);

        log.warn("Данные поступают только на ру раскладке, иначе баг !!!!!!! " + location.name()); /* на этом этапе могут поступать один и тот же город на разных языках,
         с заглавной и не с заглавной буквы, но программа не будет считывать это все как один город, вместо этого она добавит
         юзеру : "Paris, paris, Париж, париж ...", это нужно исправить. Нужно пользоваться id города, который приходит в json-е
         То есть сервис должен принимать из контроллера не location.name(), а location.idOW (id Open Weather)
         Надо будет добавить во Location, LocationEntity во все сущности, где это нужно, поле idOW (id Open Weather)
        */
        return ResponseEntity.ok()
                .body(weatherService.postLocationForUser(location.name().toLowerCase(), userId)); //сделаем чтобы location.name() из контроллера приходил в нижнем регистре, далее сделаем так, чтобы локации сохранялись в бд с нижним регистром тоже (для начала хотя бы так)
    }

    @PutMapping("/update_user/{id}")
    public ResponseEntity<User> updateUser (
            @PathVariable("id") Long id,
            @RequestBody @Valid User user
    ) {
        log.info("Updating user with id {}", user.id());

        return ResponseEntity.ok()
                .body(mainService.updateUser(id, user));
    }

    @GetMapping("/get_info_location")
    public ResponseEntity<Location> getLocation (
            @RequestBody Location location
    ) {
        log.info("Getting location " + location.name());

        //здесь та же проблема, что и в postLocation

        return ResponseEntity.ok()
                .body(mainService.getLocation(location.name()));
    }

}
