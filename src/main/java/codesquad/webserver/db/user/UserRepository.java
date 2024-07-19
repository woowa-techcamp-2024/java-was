package codesquad.webserver.db.user;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository implements UserDatabase {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users " +
                    "(id VARCHAR(255) PRIMARY KEY, " +
                    "password VARCHAR(255), " +
                    "name VARCHAR(255))");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (id, password, name)VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save user", e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("id"),
                            rs.getString("password"),
                            rs.getString("name")
                    ));
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to find user by ID", e);
            throw new RuntimeException("Failed to find user by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("password"),
                        rs.getString("name")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            logger.error("Failed to find users", e);
            throw new RuntimeException("Failed to find users", e);
        }
        return users;
    }

    @Override
    public boolean existsByUserId(String userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find user by ID", e);
            throw new RuntimeException("Failed to find user by ID", e);
        }
        return false;
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM users";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int rowsAffected = pstmt.executeUpdate();
            logger.info("Cleared {} users from the database", rowsAffected);
        } catch (SQLException e) {
            logger.error("Failed to clear users", e);
            throw new RuntimeException("Failed to clear users", e);
        }
    }
}
