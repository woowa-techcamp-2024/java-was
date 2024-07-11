package codesquad.webserver.db.user;

public class UserDatabaseFactory {

    private UserDatabaseFactory(){}

    private static class Holder {
        private static final UserDatabase INSTANCE = new InMemoryUserDatabase();
    }

    public static UserDatabase getInstance() {
        return Holder.INSTANCE;
    }
}
