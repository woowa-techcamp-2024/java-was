package repository;

import java.sql.SQLException;
import model.User;

public abstract class UserRepository {
    abstract User[] findAll();
    public abstract void addUser(User user);
    abstract void removeUser(String userId);
    public abstract User getUserById(String userId);
    static UserRepository getInstance() throws SQLException {
        return null;
    }
}
