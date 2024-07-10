package codesquad.domain;

import codesquad.domain.model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserStorage {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user) {
        users.putIfAbsent(user.getUserId(), user);
    }

    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

}
