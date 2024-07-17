package codesquad.webserver.db.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryUserDatabaseTest {

    private InMemoryUserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new InMemoryUserDatabase();
    }

    @Test
    @DisplayName("새로운 사용자를 저장하고 조회한다")
    void testSaveAndFindUser() {
        User newUser = new User("4", "password4", "4번 유저");
        userDatabase.save(newUser);

        User foundUser = userDatabase.findByUserId("4").get();
        assertNotNull(foundUser);
        assertEquals("4", foundUser.getUserId());
        assertEquals("password4", foundUser.getPassword());
        assertEquals("4번 유저", foundUser.getName());
    }

    @Test
    @DisplayName("이미 존재하는 사용자 ID로 저장 시 예외를 발생시킨다")
    void testSaveExistingUser() {
        User existingUser = new User("1", "newPassword", "새로운 1번 유저");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userDatabase.save(existingUser));
        assertEquals("User with id 1 already exists", exception.getMessage());
    }

    @Test
    @DisplayName("모든 사용자를 조회한다")
    void testFindAllUsers() {
        List<User> allUsers = userDatabase.findAllUsers();
        assertEquals(3, allUsers.size());
    }

    @Test
    @DisplayName("특정 사용자 ID가 존재하는지 확인한다")
    void testExistsByUserId() {
        assertTrue(userDatabase.existsByUserId("1"));
        assertFalse(userDatabase.existsByUserId("nonexistent"));
    }

    @Test
    @DisplayName("데이터베이스를 비운다")
    void testClear() {
        userDatabase.clear();
        List<User> allUsers = userDatabase.findAllUsers();
        assertTrue(allUsers.isEmpty());
    }
}
