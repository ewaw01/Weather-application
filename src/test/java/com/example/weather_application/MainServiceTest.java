package com.example.weather_application;

import com.example.weather_application.errors.UserAlreadyExistException;
import com.example.weather_application.location.Location;
import com.example.weather_application.location.LocationEntity;
import com.example.weather_application.mappers.LocationMapper;
import com.example.weather_application.mappers.UserMapper;
import com.example.weather_application.repos.LocationRepository;
import com.example.weather_application.repos.UserRepository;
import com.example.weather_application.services.MainService;
import com.example.weather_application.services.WeatherService;
import com.example.weather_application.user.User;
import com.example.weather_application.user.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MainServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    LocationRepository locationRepository;

    @Mock
    WeatherService weatherService;

    @Mock
    UserMapper userMapper;
    @Mock
    LocationMapper locationMapper;

    @Mock
    Utils utils;

    @InjectMocks
    MainService mainService;

    @Test
    @DisplayName("успешное добавление, юзер еще не существует")
    void addUser_UserNotExist() {
        User userInput = new User(
                null,
                "Fofa",
                null
        );
        UserEntity userEntity = new UserEntity(
                23L,
                "Fofa",
                ""
        );
        User userOutput = new User(
                23L,
                "Fofa",
                ""
        );

        Mockito.when(userRepository.findByUserId(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(userEntity);
        Mockito.when(userMapper.toDomain(Mockito.any(UserEntity.class)))
                .thenReturn(userOutput);

        User result = mainService.addUser(userInput);

        Assertions.assertEquals(userOutput, result);

        Mockito.verify(userRepository, Mockito.times(1)).findByUserId(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.times(1)).toDomain(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("юзер существует, ошибка")
    void addUser_UserExists() {
        User userInput = new User(
                null,
                "Fofa",
                null
        );
        UserEntity userEntity = new UserEntity(
                23L,
                "Fofa",
                ""
        );

        Mockito.when(userRepository.findByUserId(userInput.userId()))
                .thenReturn(Optional.of(userEntity));

        UserAlreadyExistException exception = Assertions.assertThrows(
                UserAlreadyExistException.class,
                () -> mainService.addUser(userInput)
        );

        Assertions.assertEquals("User already exists", exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByUserId(userInput.userId());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("успешное удаление")
    void deleteUser_UserExists() {
        Long uId = 23L;
        UserEntity userEntity = new UserEntity(
                23L,
                "Fofa",
                ""
        );

        Mockito.when(userRepository.findById(uId))
                .thenReturn(Optional.of(userEntity));
        Mockito.doNothing().when(userRepository).deleteById(uId);

        mainService.deleteUser(uId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(uId);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(uId);
    }

    @Test
    @DisplayName("юзера не существует - ошибка")
    void deleteUser_UserNotExists() {
        Long uId = 23L;

        Mockito.when(userRepository.findById(uId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> mainService.deleteUser(uId)
        );

        Assertions.assertEquals("User with id 23 not found", exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(uId);
        Mockito.verify(userRepository, Mockito.never()).deleteById(uId);
    }

    @Test
    @DisplayName("успешное обновление юзера")
    void updateUser_UserExists() {
        Long uId = 23L;
        User userInput = new User(
                null,
                "Fofa",
                ""
        );
        UserEntity oldUserEntity = new UserEntity(
                23L,
                "Fufa",
                "locations"
        );
        UserEntity updatedUserEntity = new UserEntity(
                23L,
                "Fofa",
                ""
        );
        User userOutput = new User(
                23L,
                "Fofa",
                ""
        );

        Mockito.when(userRepository.findById(uId))
                .thenReturn(Optional.of(oldUserEntity));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(updatedUserEntity);
        Mockito.when(userMapper.toDomain(Mockito.any(UserEntity.class)))
                .thenReturn(userOutput);

        User result = mainService.updateUser(uId, userInput);

        Assertions.assertEquals(userOutput, result);

        Mockito.verify(userRepository, Mockito.times(1)).findById(uId);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.times(1)).toDomain(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("юзер не найден")
    void updateUser_UserNotExists() {
        Long uId = 23L;
        User userInput = new User(
                null,
                "Fofa",
                ""
        );

        Mockito.when(userRepository.findById(uId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> mainService.updateUser(uId, userInput)
        );

        Assertions.assertEquals("User with id 23 not found", exception.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findById(uId);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.never()).toDomain(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("успешное удаление локации")
    void deleteLocation_LocationExists() {
        Long lId = 23L;
        LocationEntity locationEntity = new LocationEntity(
                19L,
                "москва",
                "RU",
                "пасмурно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "18"
        );

        Mockito.when(locationRepository.findById(lId))
                        .thenReturn(Optional.of(locationEntity));
        Mockito.doNothing().when(locationRepository).deleteById(lId);

        mainService.deleteLocation(lId);

        Mockito.verify(locationRepository, Mockito.times(1)).findById(lId);
        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(lId);
    }

    @Test
    @DisplayName("локации не сущесвует")
    void deleteLocation_LocationNotExists() {
        Long lId = 23L;

        Mockito.when(locationRepository.findById(lId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> mainService.deleteLocation(lId)
        );

        Assertions.assertEquals("Location with id 23 not found", exception.getMessage());

        Mockito.verify(locationRepository, Mockito.times(1)).findById(lId);
        Mockito.verify(locationRepository, Mockito.never()).deleteById(lId);
    }

    @Test
    @DisplayName("успешное получение локации, временный интервал совпадает с нынешним (то есть он не обновляется)")
    void getLocation_LocationExists_TimeIntervalEqualsNow() {
        String nameLocation = "москва";
        LocationEntity locationEntity = new LocationEntity(
                19L,
                "москва",
                "RU",
                "пасмурно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "18"
        );
        Location locationOut = new Location(
                19L,
                "москва",
                "RU",
                "пасмурно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "18"
        );

        Mockito.when(locationRepository.findByName(nameLocation))
                .thenReturn(Optional.of(locationEntity));
        Mockito.when(locationMapper.toDomain(Mockito.any(LocationEntity.class)))
                .thenReturn(locationOut);
        Mockito.when(utils.calculateTheTimeInterval(Mockito.anyString()))
                .thenReturn("18");

        Location result = mainService.getLocation(nameLocation);

        Assertions.assertEquals(locationOut, result);

        Mockito.verify(locationRepository, Mockito.times(1)).findByName(nameLocation);
        Mockito.verify(utils, Mockito.times(1)).calculateTheTimeInterval(Mockito.anyString());
        Mockito.verify(locationMapper, Mockito.times(1)).toDomain(locationEntity);
    }

    @Test
    @DisplayName("успешное получение локации, временный интервал не совпадает с нынешним (то есть он обновляется)")
    void getLocation_LocationExists_TimeIntervalNotEqualsNow() {
        String nameLocation = "москва";
        LocationEntity locationEntity = new LocationEntity(
                19L,
                "москва",
                "RU",
                "пасмурно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "18"
        );
        LocationEntity locationEntityGILOut = new LocationEntity(
                19L,
                "москва",
                "RU",
                "ясно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "00"
        );
        Location locationOut = new Location(
                19L,
                "москва",
                "RU",
                "ясно",
                "04n",
                3.03,
                79L,
                "1.68",
                1772338896L,
                1772377358L,
                "00"
        );

        Mockito.when(locationRepository.findByName(nameLocation))
                .thenReturn(Optional.of(locationEntity));
        Mockito.when(locationMapper.toDomain(Mockito.any(LocationEntity.class)))
                .thenReturn(locationOut);
        Mockito.when(utils.calculateTheTimeInterval(Mockito.anyString()))
                .thenReturn("00");
        Mockito.when(weatherService.getInfoLocation(Mockito.anyString()))
                .thenReturn(locationEntityGILOut);
        Mockito.when(locationRepository.save(Mockito.any(LocationEntity.class)))
                .thenReturn(locationEntityGILOut);

        Location result = mainService.getLocation(nameLocation);

        Assertions.assertEquals(locationOut, result);

        Mockito.verify(locationRepository, Mockito.times(1)).findByName(nameLocation);
        Mockito.verify(utils, Mockito.times(1)).calculateTheTimeInterval(Mockito.anyString());
        Mockito.verify(locationMapper, Mockito.times(1)).toDomain(Mockito.any(LocationEntity.class));
        Mockito.verify(weatherService, Mockito.times(1)).getInfoLocation(Mockito.anyString());
        Mockito.verify(locationRepository, Mockito.times(1)).save(Mockito.any(LocationEntity.class));
    }

    @Test
    @DisplayName("локация не найдена, ошибка")
    void getLocation_LocationNotExists() {
        String nameLocation = "москва";

        Mockito.when(locationRepository.findByName(nameLocation))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> mainService.getLocation(nameLocation)
        );

        Assertions.assertEquals("Location with name " + nameLocation + " not found", exception.getMessage());

        Mockito.verify(locationRepository, Mockito.times(1)).findByName(nameLocation);
        Mockito.verify(utils, Mockito.never()).calculateTheTimeInterval(Mockito.anyString());
        Mockito.verify(locationMapper, Mockito.never()).toDomain(Mockito.any(LocationEntity.class));
        Mockito.verify(weatherService, Mockito.never()).getInfoLocation(Mockito.anyString());
        Mockito.verify(locationRepository, Mockito.never()).save(Mockito.any(LocationEntity.class));
    }

}
