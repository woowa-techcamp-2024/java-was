package codesquad.webserver.db.user;

import codesquad.webserver.model.User;
import java.util.List;

public interface UserDatabase {
    void save(User user);

    User findByUserId(String userId);

    List<User> findAllUsers();

    boolean existsByUserId(String userId);

    void print();

    void clear();
}
