package codesquad.application.datahandler;

import codesquad.application.domain.User;
import codesquad.webserver.annotation.DataHandler;
import codesquad.webserver.database.csv.CsvConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DataHandler("UserDataHandlerCsv")
public class UserDataHandlerCsv implements UserDataHandler{
    private static Logger log = LoggerFactory.getLogger(UserDataHandlerCsv.class);


    static{
        try {
            Class.forName("codesquad.webserver.database.csv.CsvJdbcDriver");
            try (Connection con = CsvConnectionManager.getConnection()) {
                try (Statement stmt = con.createStatement()) {
                    String query;
                    query = "DROP TABLE IF EXISTS USERS";
                    stmt.execute(query);
                    log.info("user table has benn dropped");
                    query = "CREATE TABLE IF NOT EXISTS USERS (" +
                            "ID," +
                            "USERNAME," +
                            "PASSWORD," +
                            "NICKNAME" +
                            ")";
                    stmt.execute(query);
                    log.info("Users table created or already exists.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void insert(User user) {
        User insertUser = new User(UUID.randomUUID().toString(), "a", "b", "c");
        String sql = "INSERT INTO USERS (ID, USERNAME, PASSWORD, NICKNAME) VALUES (?, ?, ?, ?)";
        try (Connection con = CsvConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
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
        try(Connection con = CsvConnectionManager.getConnection(); Statement stmt = con.createStatement()){
            String query = "SELECT * FROM USERS";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                String id = rs.getString("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String nickname = rs.getString("NICKNAME");
                log.info("id: {} username: {} password: {} nickname: {}", id, username, password, nickname);
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
        try(Connection con = CsvConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, targetUsername);
            ResultSet rs = pstmt.executeQuery();
            User user = null;
            while (rs.next()) {
                String id = rs.getString("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String nickname = rs.getString("NICKNAME");
                user = new User(id, username, password, nickname);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
