package com.example.weather_application.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "locations")
    private String locations;

    public UserEntity(@NotNull String userId, String locations) {
        this.userId = userId;
        this.locations = locations;
    }

    public UserEntity() {
    }

    public UserEntity(Long id, String userId, String locations) {
        this.id = id;
        this.userId = userId;
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }
}
