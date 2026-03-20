package com.example.weather_application.mappers;

import com.example.weather_application.user.User;
import com.example.weather_application.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserMapper {
    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUserId(),
                userEntity.getLocations()
        );
    }

    public UserEntity toEntity(User user) {
        if (user.locations() == null || user.locations().isEmpty()) {
            throw new IllegalArgumentException("User must have at least one location");
        }

        List<String> locationsList = Arrays.stream(user.locations().split(" ")).toList();

        String newListLocations = "";

        for(String el: locationsList) {
            newListLocations += el + " ";
        }

        return new UserEntity(
                user.id(),
                user.userId(),
                newListLocations
        );
    }
}
