package codesquad;

import codesquad.configuration.Container;
import codesquad.configuration.DDLGenerator;
import codesquad.configuration.DatabaseConfiguration;
import codesquad.configuration.HandlerConfiguration;
import codesquad.domain.ArticleStorage;
import codesquad.domain.CommentStorage;
import codesquad.domain.UserStorage;
import codesquad.servlet.SessionStorage;
import codesquad.servlet.database.connection.ArticleDao;
import codesquad.servlet.database.connection.CommentDao;
import codesquad.servlet.database.connection.DatabaseConnector;
import codesquad.servlet.database.connection.UserDao;
import codesquad.webserver.Connector;

public class Main {
    public static void main(String[] args) {

        DatabaseConnector databaseConnector = DatabaseConfiguration.connectorConfig();
        DDLGenerator ddlGenerator = new DDLGenerator(databaseConnector);
        ddlGenerator.initializeDatabase();

        UserStorage userStorage = new UserDao(databaseConnector);
        SessionStorage sessionStorage = SessionStorage.getInstance();
        ArticleStorage articleStorage = new ArticleDao(databaseConnector);
        CommentStorage commentStorage = new CommentDao(databaseConnector);

        HandlerConfiguration.handlerMapperConfig(userStorage, sessionStorage, articleStorage, commentStorage);

        Container container = new Container();
        Connector connector = container.connector();
        connector.start();
    }
}
