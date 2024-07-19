package codesquad.configuration;

import codesquad.servlet.database.connection.DatabaseConnector;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DDLGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DDLGenerator.class);

    private final DatabaseConnector databaseConnector;

    public DDLGenerator(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void initializeDatabase() {
        try (Connection connection = databaseConnector.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     getClass().getClassLoader().getResourceAsStream("schema.sql")))) {
            logger.debug("Connection Info = {}", connection);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                if (line.endsWith(";")) {
                    String sql = sb.toString();
                    logger.info("Executing SQL: " + sql);
                    statement.execute(sql);
                    sb.setLength(0);
                }
            }
            logger.debug("Database schema initialized successfully.");
        } catch (Exception e) {
            logger.error("DDL Fail", e);
        }
    }

}
