package codesquad.database;

public record H2Config(String jdbcUrl, String username, String password) {

    public static H2Config standard() {
        return new H2Config("jdbc:h2:~/codestagram", "sa", "sa");
    }

    public static H2Config test() {
        return new H2Config("jdbc:h2:mem:test", "sa", "");
    }
}
