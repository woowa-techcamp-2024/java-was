package codesquad.domain;

import codesquad.domain.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public List<User> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

}
