package codesquad.database;

public abstract class H2Constants {

    public static final String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS `user` (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            userId VARCHAR(20) NOT NULL,
            nickname VARCHAR(20) NOT NULL,
            password VARCHAR(20) NOT NULL
            );
            """;
    public static final String CREATE_ARTICLE_TABLE = """
            CREATE TABLE IF NOT EXISTS `article` (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            content TEXT NOT NULL,
            imageName VARCHAR(255) NOT NULL,
            userId BIGINT NOT NULL
            );
            """;
}
