package com.devnetwork.infrastructure.config;

import com.devnetwork.application.user.GetUserUseCase;
import com.devnetwork.application.user.ListUsersUseCase;
import com.devnetwork.application.user.RegisterUserUseCase;
import com.devnetwork.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers use cases as Spring beans, so the application layer itself stays framework-free.
 */
@Configuration
public class UseCaseConfig {

    @Bean
    RegisterUserUseCase registerUserUseCase(UserRepository userRepository) {
        return new RegisterUserUseCase(userRepository);
    }

    @Bean
    GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }

    @Bean
    ListUsersUseCase listUsersUseCase(UserRepository userRepository) {
        return new ListUsersUseCase(userRepository);
    }
}
