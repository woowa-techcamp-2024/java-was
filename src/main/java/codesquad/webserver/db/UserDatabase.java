package codesquad.webserver.db;

import codesquad.webserver.model.User;

public interface UserDatabase {
    void save(User user);

    User findByUserId(String userId);

    boolean existsByUserId(String userId);

    void print();
}
