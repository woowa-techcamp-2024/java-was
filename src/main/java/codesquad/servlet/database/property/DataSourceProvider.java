package codesquad.servlet.database.property;

public class DataSourceProvider {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DataSourceProvider(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
