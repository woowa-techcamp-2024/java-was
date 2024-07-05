package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("User 생성자 테스트")
    void name() {
        User user = new User("userId", "password", "name", "email");

        assertEquals("userId", user.getUserId());
        assertEquals("password", user.getPassword());
        assertEquals("name", user.getName());
        assertEquals("email", user.getEmail());
    }
}