package com.example.weather_application.Services;

import com.example.weather_application.Repos.UserRepository;
import com.example.weather_application.User.User;
import com.example.weather_application.User.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WeatherService {
    private final UserRepository userRepository;

    public WeatherService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Void addUser(
            User user
    ) {
        if (userRepository.findByUserId(user.userId()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        userRepository.save(new UserEntity(
                user.userId(),
                new ArrayList<String>()
        ));
        return null;
    }
}
