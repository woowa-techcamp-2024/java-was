package repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRepositoryTest {

    private UserRepository userRepository = new UserRepository();

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    @DisplayName("유저 추가 테스트")
    void addUser() {
        User user = new User("testId", "testPassword", "testName", "testEmail");
        userRepository.addUser(user);

        User savedUser = userRepository.getUser("testId");

        assertAll(
            () -> assertEquals("testId", savedUser.getUserId()),
            () -> assertEquals("testPassword", savedUser.getPassword()),
            () -> assertEquals("testName", savedUser.getName()),
            () -> assertEquals("testEmail", savedUser.getEmail())
        );
    }
}