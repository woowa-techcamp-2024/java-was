package repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.User;

public class UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
