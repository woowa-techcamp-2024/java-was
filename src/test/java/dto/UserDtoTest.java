package dto;

import static org.junit.jupiter.api.Assertions.*;

import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDtoTest {

    @Test
    @DisplayName("toEntity 메서드 테스트")
    void name() {
        // given
        UserDto userDto = new UserDto("userId", "password", "name", "email");

        // when
        User user = userDto.toEntity();

        // then
        assertEquals("userId", user.getUserId());
        assertEquals("password", user.getPassword());
        assertEquals("name", user.getName());
        assertEquals("email", user.getEmail());
    }
}