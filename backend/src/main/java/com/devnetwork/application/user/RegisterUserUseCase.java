package com.devnetwork.application.user;

import com.devnetwork.domain.user.EmailAlreadyUsedException;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

/**
 * Use case. Depends only on the domain; wired into Spring by infrastructure config.
 */
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(RegisterUserCommand command) {
        User user = User.register(command.email(), command.displayName());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyUsedException(user.getEmail());
        }
        return userRepository.save(user);
    }
}
