package codesquad.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class UserDataBase {

    private static UserDataBase instance;

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private UserDataBase() {
    }

    public static UserDataBase getInstance() {
        if (instance == null) {
            instance = new UserDataBase();
        }
        return instance;
    }

    public boolean addUser(User user) {
        if (users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    public Optional<User> findUser(String userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }
}
