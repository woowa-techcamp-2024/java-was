package service;

import dto.UserDto;
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
        if (userRepository.getUser(userDto.userId()) != null) {
            logger.error("User already exists: {}", userDto.userId());
            return;
        }

        userRepository.addUser(userDto.toEntity());

        logger.info("User created: {}", userRepository.getUser(userDto.userId()));
    }
}
