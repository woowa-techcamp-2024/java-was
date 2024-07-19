package codesquad.webserver.database.csv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CsvConnectionManager {
    private static final String url = "jdbc:csv:./mycsv";

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
