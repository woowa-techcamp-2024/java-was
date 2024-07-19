package codesquad.database;

import codesquad.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class UserRepository {

    private static UserRepository instance;

    private final JdbcTemplate jdbcTemplate;

    private UserRepository() {
        jdbcTemplate = JdbcTemplate.getInstance();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public void save(User user) {
        final String sql = "INSERT INTO `user` (userId, nickname, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getNickname(), user.getPassword());
    }

    public Optional<User> findByUserId(String userId) {
        if (Objects.isNull(userId) || userId.isEmpty()) {
            return Optional.empty();
        }

        final String sql = "SELECT * FROM `user` WHERE userId = ?";
        User user = jdbcTemplate.queryForObject(sql, User.class, userId);
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM `user`";
        return jdbcTemplate.queryForList(sql, User.class);
    }

    public void deleteAll() {
        final String sql = "DELETE FROM `user`";
        jdbcTemplate.update(sql);
    }
}
