package codesquad.webserver.db.user;

import codesquad.webserver.annotation.Component;
import codesquad.webserver.db.csv.CsvDatabaseInitializer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CsvUserRepository implements UserDatabase {

    private static final Logger logger = LoggerFactory.getLogger(CsvUserRepository.class);
    private static final String CSV_FILE_NAME = "users.csv";
    private static final String CSV_HEADER = "id,password,name";
    private static final Map<String, Integer> COLUMN_INDICES;
    private final String jdbcUrl;

    static {
        String[] headers = CSV_HEADER.split(",");
        COLUMN_INDICES = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            COLUMN_INDICES.put(headers[i], i);
        }
        try {
            Class.forName("codesquad.webserver.db.csv.CsvDriver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Failed to load CSV JDBC driver: " + e.getMessage());
        }
    }

    public int getColumnIndex(String columnName) {
        Integer index = COLUMN_INDICES.get(columnName);
        if (index == null) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        return index + 1;
    }

    public CsvUserRepository() {
        try {
            String filePath = CsvDatabaseInitializer.initializeDatabase(CSV_FILE_NAME, CSV_HEADER);
            this.jdbcUrl = CsvDatabaseInitializer.getJdbcUrl(filePath);
            logger.info("JDBC URL: {}", jdbcUrl);
        } catch (IOException e) {
            logger.error("Failed to initialize user repository", e);
            throw new RuntimeException("Failed to initialize user repository", e);
        }
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (id, password, name)VALUES (?, ?, ?)";
        if (existsByUserId(user.getUserId())) {
            throw new SQLException("Already exist user");
        }
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
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
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString(getColumnIndex("id")),
                            rs.getString(getColumnIndex("password")),
                            rs.getString(getColumnIndex("name"))
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
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement pstmt = conn.createStatement();
             ResultSet rs = pstmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                        rs.getString(getColumnIndex("id")),
                        rs.getString(getColumnIndex("password")),
                        rs.getString(getColumnIndex("name"))
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
        String sql = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString(getColumnIndex("id"));
                if (userId.equals(id)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to check if user exists", e);
            throw new RuntimeException("Failed to check if user exists", e);
        }
        return false;
    }

    @Override
    public void clear() {
        // 모든 내용 삭제임
        String sql = "DELETE FROM users";
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            logger.info("Cleared {} users from the database", rowsAffected);
        } catch (SQLException e) {
            logger.error("Failed to clear users", e);
            throw new RuntimeException("Failed to clear users", e);
        }
    }
}
