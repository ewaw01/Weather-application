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
    private List<String> locations;

    public UserEntity(@NotNull String userId, List<String> locations) {
        this.userId = userId;
        this.locations = locations;
    }

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
