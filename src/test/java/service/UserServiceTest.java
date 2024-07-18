package service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dto.UserDto;
import java.sql.SQLException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepositoryMemory;

class UserServiceTest {
    private UserService userService;
    private UserRepositoryMemory userRepositoryMemory;
    private UserDto userDto = new UserDto("userId", "password", "name", "email");

    @BeforeEach
    void setUp() {
        userRepositoryMemory = UserRepositoryMemory.getInstance();
        userService = new UserService(userRepositoryMemory);
    }

    @Test
    void createUser() throws SQLException {
        userService.createUser(userDto);

        User savedUser = userRepositoryMemory.getUserById("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), savedUser.getUserId()),
            () -> assertEquals(userDto.password(), savedUser.getPassword()),
            () -> assertEquals(userDto.name(), savedUser.getName()),
            () -> assertEquals(userDto.email(), savedUser.getEmail())
        );


    }

    @Test
    void findUserById() throws SQLException {
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
    void createUserWithExistingId() throws SQLException {
        userService.createUser(userDto);
        userService.createUser(userDto);

        User savedUser = userRepositoryMemory.getUserById("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), savedUser.getUserId()),
            () -> assertEquals(userDto.password(), savedUser.getPassword()),
            () -> assertEquals(userDto.name(), savedUser.getName()),
            () -> assertEquals(userDto.email(), savedUser.getEmail())
        );
    }

    @Test
    void findUserByIdWithNonExistingId() throws SQLException {
        User foundUser = userService.findUserById("nonExistingId");

        assertNull(foundUser);
    }
}