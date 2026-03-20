package com.example.weather_application.services;

import com.example.weather_application.errors.UserAlreadyExistException;
import com.example.weather_application.location.Location;
import com.example.weather_application.location.LocationEntity;
import com.example.weather_application.location.LocationSearchFilter;
import com.example.weather_application.mappers.LocationMapper;
import com.example.weather_application.repos.LocationRepository;
import com.example.weather_application.repos.UserRepository;
import com.example.weather_application.user.User;
import com.example.weather_application.user.UserEntity;
import com.example.weather_application.mappers.UserMapper;
import com.example.weather_application.user.UserSearchFilter;
import com.example.weather_application.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MainService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    private final WeatherService weatherService;

    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    private final Utils utils;

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    public MainService(UserRepository userRepository, LocationRepository locationRepository, WeatherService weatherService, UserMapper userMapper, LocationMapper locationMapper, Utils utils) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.weatherService = weatherService;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this.utils = utils;
    }

    public User addUser( //!напиши проверку на id и location, при создании юзера можно вводить только userId
            User user
    ) {
        log.info("Adding new user");

        if (userRepository.findByUserId(user.userId()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }

        UserEntity userEntity = new UserEntity(
                user.userId(),
                ""
        );

        userRepository.save(userEntity);

        return userMapper.toDomain(userEntity);
    }

    public Void deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        log.info("Deleting user with id " + id);

        userRepository.deleteById(id);
        return null;
    }

    public Location getLocation(
            String nameLocation
    ) {
        log.info("Getting location with name " + nameLocation);

        LocationEntity location;
        if(locationRepository.findByName(nameLocation.toLowerCase()).isPresent()) {
            LocationEntity oldLocation = locationRepository.findByName(nameLocation.toLowerCase()).get();

            if(!utils.calculateTheTimeInterval(LocalDateTime.now()
                    .toString().split("T")[1].substring(0, 2)).equals(oldLocation.getTime())) {
                location = locationRepository.save(weatherService.getInfoLocation(nameLocation.toLowerCase()));
            } else {
                location = oldLocation;
            }
        } else {
            location = locationRepository
                    .save(weatherService.getInfoLocation(nameLocation.toLowerCase()));
        }

        return locationMapper.toDomain(location);
    }

    public Void deleteLocation(
            Long id
    ) {
        log.info("Deleting location with id " + id);

        if (locationRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);
        return null;
    }

    public User updateUser(
            Long id,
            User user
    ) {
        log.info("Updating user with id " + id);

        if (userRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }

        var updatedUser = userRepository.save(new UserEntity(
                id,
                user.userId(),
                user.locations()
        ));

        return userMapper.toDomain(updatedUser);
    }

    public List<User> searchAllUsersByFilter(
            UserSearchFilter filter
    ) {
        log.info("Searching all users with filter");

        int pageNum = filter.pageNum() == null ?
                0 : filter.pageNum();
        int pageSize = filter.pageSize() == null ?
                10 : filter.pageSize();

        var pageable = PageRequest.of(pageNum, pageSize);

        Page<UserEntity> pageResult = userRepository.findAllByFilter(
                filter.id(),
                filter.userId(),
                pageable
        );

        return pageResult.stream()
                .map(userMapper::toDomain)
                .toList();
    }

    public List<Location> searchAllLocationsByFilter(
            LocationSearchFilter filter
    ) {
        log.info("Searching all locations with filter");

        int pageNum = filter.pageNum() == null ?
                0 : filter.pageNum();
        int pageSize = filter.pageSize() == null ?
                10 : filter.pageSize();

        var pageable = PageRequest.of(pageNum, pageSize);

        Page<LocationEntity> pageResult = locationRepository.findAllByFilter(
                filter.id(),
                filter.name(),
                filter.country(),
                filter.description(),
                filter.icon(),
                filter.temperature(),
                filter.humidity(),
                filter.windSpeed(),
                filter.sunrise(),
                filter.sunset(),
                filter.time(),
                pageable
        );

        return pageResult.stream()
                .map(locationMapper::toDomain)
                .toList();
    }
}
