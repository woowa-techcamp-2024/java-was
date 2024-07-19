package codesquad.webserver.db.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CsvUserRepositoryTest {

    private UserDatabase userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new CsvUserRepository();
        userRepository.clear();
    }

    @AfterEach
    void tearDown() {
        userRepository.clear();
    }

    @Test
    @DisplayName("사용자를 저장하고 해당 사용자를 찾는다.")
    void saveAndFindByUserId() throws SQLException {
        User user = new User("testUser", "password", "Test User");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUserId("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUserId());
        assertEquals("password", foundUser.get().getPassword());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    void findByUserIdNotFound() {
        Optional<User> notFoundUser = userRepository.findByUserId("nonexistentUser");
        assertFalse(notFoundUser.isPresent());
    }

    @Test
    @DisplayName("모든 사용자를 찾아 리스트로 반환한다.")
    void findAllUsers() throws SQLException {
        User user1 = new User("user1", "password1", "User One");
        User user2 = new User("user2", "password2", "User Two");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getUserId().equals("user1")));
        assertTrue(users.stream().anyMatch(u -> u.getUserId().equals("user2")));
    }

    @Test
    @DisplayName("id를 통해 사용자가 존재하는지 확인한다.")
    void existsByUserId() throws SQLException {
        User user = new User("existingUser", "password", "Existing User");
        userRepository.save(user);

        assertTrue(userRepository.existsByUserId("existingUser"));
        assertFalse(userRepository.existsByUserId("nonexistentUser"));
    }

    @Test
    @DisplayName("모든 사용자를 지운다.")
    void clear() throws SQLException {
        User user = new User("userToClear", "password", "User To Clear");
        userRepository.save(user);
        assertFalse(userRepository.findAllUsers().isEmpty());

        userRepository.clear();
        assertTrue(userRepository.findAllUsers().isEmpty());
    }
}