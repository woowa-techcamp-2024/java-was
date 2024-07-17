package codesquad.webserver.db.user;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryUserDatabase implements UserDatabase {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserDatabase.class);
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public InMemoryUserDatabase() {
        users.put("1", new User("1", "1", "1번 유저"));
        users.put("2", new User("2", "2", "2번 유저"));
        users.put("3", new User("3", "3", "3번 유저"));
    }

    @Override
    public void save(User user) {
        User existingUser = users.putIfAbsent(user.getUserId(), user);
        if (existingUser != null) {
            throw new IllegalArgumentException("User with id " + user.getUserId() + " already exists");
        }
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAllUsers() {
        return users.values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsByUserId(String userId) {
        return users.containsKey(userId);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
