package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    private Connection connection;
    private String url = "jdbc:h2:tcp://localhost:9092/~/test;INIT=RUNSCRIPT FROM 'classpath:schema.sql'";
    private String user = "sa";
    private String password = "";

    private DatabaseConnector() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLException("Failed to create the database connection.", e);
        }
    }

    public static DatabaseConnector getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnector();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnector();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
