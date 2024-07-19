package codesquad.webserver.database.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.Properties;

public class CsvJdbcDriver implements Driver {
    private Logger log = LoggerFactory.getLogger(CsvJdbcDriver.class);

    static{
        try{
            DriverManager.registerDriver(new CsvJdbcDriver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if(!acceptsURL(url)){
            return null;
        }
        String csvFilePath = url.substring("jdbc:csv:".length());
        File file = new File(csvFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
        return new CsvConnection(url);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith("jdbc:csv:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
