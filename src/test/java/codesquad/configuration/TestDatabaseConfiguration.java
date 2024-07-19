package codesquad.configuration;

import codesquad.servlet.database.connection.DatabaseConnector;
import codesquad.servlet.database.property.DataSourceProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDatabaseConfiguration {

    public static DataSourceProvider dataSourceProvider = new DataSourceProvider("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            "sa", "");
    public static DatabaseConnector databaseConnector = new DatabaseConnector(dataSourceProvider);

    public static DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public static void initializeDatabase() {
        try (Connection connection = databaseConnector.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     TestDatabaseConfiguration.class.getResourceAsStream("/ddl.sql")))) {

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            statement.execute(sql.toString());

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
