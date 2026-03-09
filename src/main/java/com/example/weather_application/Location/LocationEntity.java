package com.example.weather_application.Location;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "cache_locations")
@Entity
public class LocationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "country")
    public String country;
    @Column(name = "description")
    public String description;
    @Column(name = "icon")
    public String icon;
    @Column(name = "temperature")
    public Double temperature;
    @Column(name = "humidity")
    public Long humidity;
    @Column(name = "windSpeed")
    public String windSpeed;
    @Column(name = "sunrise")
    public Long sunrise;
    @Column(name = "sunset")
    public Long sunset;
    @Column(name = "time")
    public String time;

    public LocationEntity() {
    }

    public LocationEntity(Long id, String name, String country, String description, String icon, Double temperature, Long humidity, String windSpeed, Long sunrise, Long sunset, String time) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Long getHumidity() {
        return humidity;
    }

    public void setHumidity(Long humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }
}
