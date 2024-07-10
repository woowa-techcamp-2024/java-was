package service;

import dto.UserDto;
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

    public void createUser(UserDto userDto) {
        if (userRepository.getUserById(userDto.userId()) != null) {
            logger.error("User already exists: {}", userDto.userId());
            return;
        }

        userRepository.addUser(userDto.toEntity());

        logger.info("User created: {}", userRepository.getUserById(userDto.userId()));
    }

    public User findUserById(String userId) {
        return userRepository.getUserById(userId);
    }
}
