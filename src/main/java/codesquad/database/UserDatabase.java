package codesquad.database;

import codesquad.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {

    private final static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public UserDatabase() {
        for(int i = 0; i < 5; i++) {
            users.put("user" + i, new User(i, "user" + i, "password" + i, "name" + i));
        }
    }

    public Optional<User> findUserById(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public List<User> getAllUser() {
        return List.copyOf(users.values());
    }

}
