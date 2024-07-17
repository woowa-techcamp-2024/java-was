package codesquad.webserver.database;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final String url = "jdbc:h2:file:./db";
    private static final String user = "sa";
    private static final String password = "";
    private static final JdbcConnectionPool jdbcConnectionPool = JdbcConnectionPool.create(url, user, password);

    public static Connection getConnection() throws SQLException {
        return jdbcConnectionPool.getConnection();
    }
}
