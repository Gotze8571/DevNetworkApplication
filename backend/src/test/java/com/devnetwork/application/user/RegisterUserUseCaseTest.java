package com.devnetwork.application.user;

import com.devnetwork.domain.user.EmailAlreadyUsedException;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Use cases are plain Java, so they are tested with an in-memory fake instead of Spring or a database.
 */
class RegisterUserUseCaseTest {

    private final InMemoryUserRepository repository = new InMemoryUserRepository();
    private final RegisterUserUseCase useCase = new RegisterUserUseCase(repository);

    @Test
    void registersUserWithNormalizedEmail() {
        User user = useCase.execute(new RegisterUserCommand("  Ada@Example.com ", "Ada"));

        assertThat(user.getEmail()).isEqualTo("ada@example.com");
        assertThat(repository.findById(user.getId())).isPresent();
    }

    @Test
    void rejectsDuplicateEmail() {
        useCase.execute(new RegisterUserCommand("ada@example.com", "Ada"));

        assertThatThrownBy(() -> useCase.execute(new RegisterUserCommand("ADA@example.com", "Ada 2")))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    @Test
    void rejectsBlankDisplayName() {
        assertThatThrownBy(() -> useCase.execute(new RegisterUserCommand("ada@example.com", " ")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class InMemoryUserRepository implements UserRepository {

        private final List<User> users = new ArrayList<>();

        @Override
        public User save(User user) {
            users.add(user);
            return user;
        }

        @Override
        public Optional<User> findById(UUID id) {
            return users.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public boolean existsByEmail(String email) {
            return users.stream().anyMatch(u -> u.getEmail().equals(email));
        }

        @Override
        public List<User> findAll() {
            return List.copyOf(users);
        }
    }
}
