package codesquad.servlet.database.property;

public enum DataSource {

    JDBC_URL("jdbc:h2:~/test"),
    USER_NAME("sa"),
    PASSWORD("");

    private final String property;

    DataSource(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public String getPropertyToUppercase() {
        return property.toUpperCase();
    }
}
