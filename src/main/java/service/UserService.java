package service;

import dto.UserDto;
import java.sql.SQLException;
import model.User;
import org.slf4j.Logger;
import repository.UserRepository;
import util.LoggerUtil;

public class UserService {
    private static final Logger logger = LoggerUtil.getLogger();

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserDto userDto) throws SQLException {
        logger.info("create user in progress");
        if (userRepository.getUserById(userDto.userId()) != null) {
            logger.error("User already exists: {}", userDto.userId());
            return;
        }

        if (userDto.userId() == null) {
            logger.error("User ID is null");
            return;
        }

        if (userDto.password() == null) {
            logger.error("Password is null");
            return;
        }

        if (userDto.name() == null) {
            logger.error("Name is null");
            return;
        }

        userRepository.addUser(userDto.toEntity());

        logger.info("User created: {}", userRepository.getUserById(userDto.userId()));
    }

    public User findUserById(String userId) throws SQLException {
        return userRepository.getUserById(userId);
    }
}
