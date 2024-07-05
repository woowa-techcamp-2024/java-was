package service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void createUser() {
        userService.createUser(userDto);

        User savedUser = userRepository.getUser("userId");

        assertAll(
            () -> assertEquals(userDto.userId(), savedUser.getUserId()),
            () -> assertEquals(userDto.password(), savedUser.getPassword()),
            () -> assertEquals(userDto.name(), savedUser.getName()),
            () -> assertEquals(userDto.email(), savedUser.getEmail())
        );


    }

}