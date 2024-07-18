package codesquad.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    public static void initialize() {
        H2Config h2Config = H2Config.standard();
        try (Connection connection = DriverManager.getConnection(h2Config.jdbcUrl(), h2Config.username(), h2Config.password());
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(H2Constants.CREATE_USER_TABLE);
            statement.executeUpdate(H2Constants.CREATE_ARTICLE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
