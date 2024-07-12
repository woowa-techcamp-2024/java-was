package codesquad.webserver.db.user;

public class UserDatabaseFactory {
//TODO: 삭제예정
    private UserDatabaseFactory(){}

    private static class Holder {
        private static final UserDatabase INSTANCE = new InMemoryUserDatabase();
    }

    public static UserDatabase getInstance() {
        return Holder.INSTANCE;
    }
}
