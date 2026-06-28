package com.productivitytracker.mapper;

import com.productivitytracker.dto.UserDTO;
import com.productivitytracker.model.User;

/**
 * Converts User model objects into safe DTOs.
 */
public final class UserMapper {

    private UserMapper() {
        throw new AssertionError("UserMapper should not be instantiated");
    }

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
