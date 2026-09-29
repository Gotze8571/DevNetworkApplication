package com.devnetwork.application.user;

import com.devnetwork.application.auth.PasswordHasher;
import com.devnetwork.domain.user.EmailAlreadyUsedException;
import com.devnetwork.domain.user.PasswordPolicy;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

/**
 * Use case. Depends only on the domain; wired into Spring by infrastructure config.
 */
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public User execute(RegisterUserCommand command) {
        PasswordPolicy.validate(command.password());
        String email = User.normalizeEmail(command.email());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }
        User user = User.register(email, command.displayName(), passwordHasher.hash(command.password()));
        return userRepository.save(user);
    }
}
