package codesquad.configuration;

import static codesquad.servlet.database.property.DataSource.JDBC_URL;
import static codesquad.servlet.database.property.DataSource.PASSWORD;
import static codesquad.servlet.database.property.DataSource.USER_NAME;

import codesquad.servlet.database.connection.DatabaseConnector;
import codesquad.servlet.database.property.DataSourceProvider;

public class DatabaseConfiguration {

    public static DatabaseConnector connectorConfig() {
        DataSourceProvider dataSourceProvider = new DataSourceProvider(JDBC_URL.getProperty(), USER_NAME.getProperty(),
                PASSWORD.getProperty());
        return new DatabaseConnector(dataSourceProvider);
    }

}
