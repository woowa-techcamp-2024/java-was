package repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.User;
import org.slf4j.Logger;
import util.LoggerUtil;

public class UserRepository {
    private static final Logger logger = LoggerUtil.getLogger();
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private static UserRepository instance= new UserRepository();

    private UserRepository() {
    }

    public User[] findAll() {
        return users.values().toArray(new User[0]);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getUserId())) {
            return;
        }
        logger.info("add user: {}", user.getUserId());
        users.put(user.getUserId(), user);
        for (String key : users.keySet()) {
            logger.info("userId List: {}", key);
        }
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public User getUserById(String userId) {
        logger.info("userId: {}", userId);
        for (String key : users.keySet()) {
            logger.info("key: {}", key);
            if (key.equals(userId)) {
                return users.get(key);
            }
        }
        return null;
    }

    public static UserRepository getInstance() {
        return instance;
    }
}
