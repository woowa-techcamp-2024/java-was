package dto;

import model.User;

public record UserDto(
    String userId,
    String password,
    String name,
    String email
) {

    public User toEntity() {
        return new User(userId, password, name, email);
    }
}
