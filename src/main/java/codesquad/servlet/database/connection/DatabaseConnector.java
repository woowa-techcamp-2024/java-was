package codesquad.servlet.database.connection;

import codesquad.servlet.database.property.DataSourceProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private final DataSourceProvider dataSourceProvider;

    public DatabaseConnector(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dataSourceProvider.getJdbcUrl(),
                dataSourceProvider.getUsername(),
                dataSourceProvider.getPassword());
    }

}
