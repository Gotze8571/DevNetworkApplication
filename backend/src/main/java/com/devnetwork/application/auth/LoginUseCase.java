package com.devnetwork.application.auth;

import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

import java.util.Optional;

/**
 * Checks a user's credentials. Starting the session is the presentation layer's job.
 */
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    /** Compared against when the email is unknown, so both failure cases take about the same time. */
    private final String dummyHash;

    public LoginUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.dummyHash = passwordHasher.hash("dummy-password-for-timing");
    }

    public User execute(LoginCommand command) {
        if (command.email() == null || command.password() == null || !command.email().contains("@")) {
            throw new InvalidCredentialsException();
        }
        Optional<User> user = userRepository.findByEmail(User.normalizeEmail(command.email()));
        String hash = user.map(User::getPasswordHash).orElse(dummyHash);
        if (!passwordHasher.matches(command.password(), hash) || user.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        return user.get();
    }
}
