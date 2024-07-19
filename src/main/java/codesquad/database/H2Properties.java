package codesquad.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class H2Properties {

    private static final String url;
    private static final String username;
    private static final String password;

    static {
        Properties properties = new Properties();
        try (InputStream inputStream = H2Properties.class.getResourceAsStream("/application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        url = properties.getProperty("db.url");
        username = properties.getProperty("db.username");
        password = properties.getProperty("db.password");
    }

    private H2Properties() {
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
