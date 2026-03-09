package com.example.weather_application;

import com.example.weather_application.Location.LocationEntity;
import com.example.weather_application.Mappers.LocationMapper;
import com.example.weather_application.Mappers.UserMapper;
import com.example.weather_application.Repos.LocationRepository;
import com.example.weather_application.Repos.UserRepository;
import com.example.weather_application.Services.WeatherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    LocationRepository locationRepository;

    @Mock
    Utils utils;

    @Mock
    UserMapper userMapper;

    @Mock
    LocationMapper locationMapper;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    WeatherService weatherService;

    Map<String, Object> mockResponse = Map.of(
            "name", "London",
            "sys", Map.of(
                    "country", "GB",
                    "sunrise", 1717890123,
                    "sunset", 1717945678
            ),
            "weather", List.of(
                    Map.of(
                            "description", "broken clouds",
                            "icon", "04d"
                    )
            ),
            "main", Map.of(
                    "temp", 15.5,
                    "humidity", 72
            ),
            "wind", Map.of(
                    "speed", 5.5
            )
    );

//    @BeforeEach
//    void setUp() {
//        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
//        ReflectionTestUtils.setField(weatherService, "baseUrl", "https://api.openweathermap.org/data/2.5");
//        ReflectionTestUtils.setField(weatherService, "defaultUnits", "metric");
//        ReflectionTestUtils.setField(weatherService, "defaultLang", "ru");
//    }
//
//    @Test
//    @DisplayName("получение информации по локации, локация уже существует")
//    void getInfoLocation_LocationExist() {
//        String locationName = "london";
//
//        LocationEntity locationEntity = new LocationEntity(
//                23L,
//                "london",
//                "GB",
//                "broken clouds",
//                "04d",
//                15.5,
//                72L,
//                "5.5",
//                1717890123L,
//                1717945678L,
//                "15"
//        );
//
//        ResponseEntity<Map> mockResponseEntity = Mockito.mock(ResponseEntity.class);
//
//        Mockito.when(mockResponseEntity.getBody()).thenReturn(mockResponse);
//        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Map.class)))
//                .thenReturn(mockResponseEntity);
//        Mockito.when(locationRepository.findByName(Mockito.anyString()))
//                .thenReturn(Optional.of(locationEntity));
//        Mockito.when(utils.calculateTheTimeInterval(Mockito.anyString()))
//                .thenReturn("15");
//
//        LocationEntity result = weatherService.getInfoLocation(locationName);
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(locationEntity, result);
//
//        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(Mockito.anyString(), Mockito.eq(Map.class));
//        Mockito.verify(locationRepository, Mockito.times(1)).findByName(Mockito.anyString());
//        Mockito.verify(utils, Mockito.times(1)).calculateTheTimeInterval(Mockito.anyString());
//    }
//
//    @Test
//    @DisplayName("получение информации по локации, локации не существует, в бд добавится новая локация по имени")
//    void getInfoLocation_LocationNotExist() {
//
//    }
}
