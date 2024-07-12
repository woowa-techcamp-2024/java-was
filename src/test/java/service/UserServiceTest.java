package service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dto.UserDto;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private UserDto userDto = new UserDto("userId", "password", "name", "email");

    @BeforeEach
    void setUp() {
        userRepository = UserRepository.getInstance();
        userService = new UserService(userRepository);
    }

    @Test
    void createUser() {
        userService.createUser(userDto);

        User savedUser = userRepository.getUserById("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), savedUser.getUserId()),
            () -> assertEquals(userDto.password(), savedUser.getPassword()),
            () -> assertEquals(userDto.name(), savedUser.getName()),
            () -> assertEquals(userDto.email(), savedUser.getEmail())
        );


    }

    @Test
    void findUserById() {
        userService.createUser(userDto);

        User foundUser = userService.findUserById("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), foundUser.getUserId()),
            () -> assertEquals(userDto.password(), foundUser.getPassword()),
            () -> assertEquals(userDto.name(), foundUser.getName()),
            () -> assertEquals(userDto.email(), foundUser.getEmail())
        );
    }

    @Test
    void createUserWithExistingId() {
        userService.createUser(userDto);
        userService.createUser(userDto);

        User savedUser = userRepository.getUserById("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), savedUser.getUserId()),
            () -> assertEquals(userDto.password(), savedUser.getPassword()),
            () -> assertEquals(userDto.name(), savedUser.getName()),
            () -> assertEquals(userDto.email(), savedUser.getEmail())
        );
    }

    @Test
    void findUserByIdWithNonExistingId() {
        User foundUser = userService.findUserById("nonExistingId");

        assertNull(foundUser);
    }
}