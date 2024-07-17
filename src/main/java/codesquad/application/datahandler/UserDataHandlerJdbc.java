package codesquad.application.datahandler;

import codesquad.application.domain.User;
import codesquad.webserver.annotation.DataHandler;
import codesquad.webserver.database.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataHandler("UserDataHandlerJdbc")
public class UserDataHandlerJdbc implements UserDataHandler {
    private static final Logger log = LoggerFactory.getLogger(UserDataHandlerJdbc.class);

    static {
        try (Connection con = DatabaseConnectionManager.getConnection(); Statement stmt = con.createStatement()) {
            String query = "DROP TABLE IF EXISTS USERS";
            stmt.execute(query);
            log.info("user table has benn dropped");
            query = "CREATE TABLE IF NOT EXISTS USERS (" +
                    "ID VARCHAR(36) PRIMARY KEY," +
                    "USERNAME VARCHAR(255) NOT NULL," +
                    "PASSWORD VARCHAR(255) NOT NULL," +
                    "NICKNAME VARCHAR(255) NOT NULL" +
                    ")";
            stmt.execute(query);
            log.info("Users table created or already exists.");

            ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM USERS");
            while (rs.next()) {
                log.info("Column: " + rs.getString("FIELD"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void insert(User user) {
        String key = UUID.randomUUID().toString();
        User insertUser = new User(key, user.getUsername(), user.getPassword(), user.getNickname());
        String sql = "INSERT INTO USERS (ID, USERNAME, PASSWORD, NICKNAME) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, insertUser.getId());
            pstmt.setString(2, insertUser.getUsername());
            pstmt.setString(3, insertUser.getPassword());
            pstmt.setString(4, insertUser.getNickname());
            int affectedRows = pstmt.executeUpdate();
            log.info("Inserted user: " + insertUser.toString() + ", Affected rows: " + affectedRows);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection(); Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM USERS";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String nickname = rs.getString("NICKNAME");
                users.add(new User(id, username, password, nickname));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return users;
    }

    @Override
    public Optional<User> getByUsername(String targetUsername) {
        String sql = "SELECT * FROM USERS WHERE USERS.USERNAME = ?";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, targetUsername);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String nickname = rs.getString("NICKNAME");
                User user = new User(id, username, password, nickname);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
