package codesquad.webserver.model;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.db.user.User;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user1", "password1", "John Doe");
    }

    @Test
    @DisplayName("User 객체를 올바르게 생성한다")
    void testUserCreation() {
        assertThat(user.getUserId()).isEqualTo("user1");
        assertThat(user.getPassword()).isEqualTo("password1");
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("User 객체를 Map으로부터 올바르게 생성한다")
    void testUserCreationFromMap() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", "user2");
        userInfo.put("password", "password2");
        userInfo.put("name", "Jane Doe");

        User userFromMap = User.of(userInfo);

        assertThat(userFromMap.getUserId()).isEqualTo("user2");
        assertThat(userFromMap.getPassword()).isEqualTo("password2");
        assertThat(userFromMap.getName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("User 객체의 toString 메서드가 올바르게 동작한다")
    void testToString() {
        String expected = "User{userId='user1', password='password1', name='John Doe'}";
        assertThat(user.toString()).isEqualTo(expected);
    }
}
