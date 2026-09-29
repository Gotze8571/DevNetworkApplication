package com.devnetwork.application.user;

import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserNotFoundException;
import com.devnetwork.domain.user.UserRepository;

import java.util.UUID;

public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
