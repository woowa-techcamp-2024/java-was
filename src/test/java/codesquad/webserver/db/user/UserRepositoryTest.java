package codesquad.webserver.db.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("사용자 저장소 테스트")
class UserRepositoryTest {

    private DataSource dataSource;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws SQLException {
        // H2 인메모리 데이터베이스 설정
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        this.dataSource = ds;

        this.userRepository = new UserRepository(dataSource);

        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute("DELETE FROM users");
        }
    }

    @Test
    @DisplayName("사용자를 저장하고 조회한다")
    void 사용자를_저장하고_조회한다() {
        // Given
        User user = new User("testuser", "password", "Test User");

        // When
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUserId("testuser");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUserId()).isEqualTo("testuser");
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 조회하면 빈 Optional을 반환한다")
    void 존재하지_않는_사용자_조회() {
        // When
        Optional<User> foundUser = userRepository.findByUserId("nonexistent");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("모든 사용자를 조회한다")
    void 모든_사용자를_조회한다() {
        // Given
        userRepository.save(new User("user1", "pass1", "User 1"));
        userRepository.save(new User("user2", "pass2", "User 2"));

        // When
        List<User> users = userRepository.findAllUsers();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUserId).containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @DisplayName("사용자 존재 여부를 확인한다")
    void 사용자_존재_여부를_확인한다() {
        // Given
        userRepository.save(new User("existinguser", "pass", "Existing User"));

        // When & Then
        assertThat(userRepository.existsByUserId("existinguser")).isTrue();
        assertThat(userRepository.existsByUserId("nonexistentuser")).isFalse();
    }

    @Test
    @DisplayName("사용자 데이터를 초기화한다")
    void 사용자_데이터를_초기화한다() {
        // Given
        userRepository.save(new User("user1", "pass1", "User 1"));
        userRepository.save(new User("user2", "pass2", "User 2"));

        // When
        userRepository.clear();

        // Then
        List<User> users = userRepository.findAllUsers();
        assertThat(users).isEmpty();
    }
}