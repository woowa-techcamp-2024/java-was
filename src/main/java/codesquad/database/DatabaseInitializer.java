package codesquad.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    public static void initialize() {
        String url = H2Properties.getUrl();
        String username = H2Properties.getUsername();
        String password = H2Properties.getPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(H2Constants.CREATE_USER_TABLE);
            statement.executeUpdate(H2Constants.CREATE_ARTICLE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
