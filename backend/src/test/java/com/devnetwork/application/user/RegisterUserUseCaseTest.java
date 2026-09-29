package com.devnetwork.application.user;

import com.devnetwork.domain.user.EmailAlreadyUsedException;
import com.devnetwork.domain.user.User;
import com.devnetwork.support.FakePasswordHasher;
import com.devnetwork.support.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Use cases are plain Java, so they are tested with in-memory fakes instead of Spring or a database.
 */
class RegisterUserUseCaseTest {

    private final InMemoryUserRepository repository = new InMemoryUserRepository();
    private final RegisterUserUseCase useCase = new RegisterUserUseCase(repository, new FakePasswordHasher());

    @Test
    void registersUserWithNormalizedEmailAndHashedPassword() {
        User user = useCase.execute(new RegisterUserCommand("  Ada@Example.com ", "Ada", "correct-horse"));

        assertThat(user.getEmail()).isEqualTo("ada@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("hashed:correct-horse");
        assertThat(repository.findById(user.getId())).isPresent();
    }

    @Test
    void rejectsDuplicateEmail() {
        useCase.execute(new RegisterUserCommand("ada@example.com", "Ada", "correct-horse"));

        assertThatThrownBy(() -> useCase.execute(new RegisterUserCommand("ADA@example.com", "Ada 2", "correct-horse")))
                .isInstanceOf(EmailAlreadyUsedException.class);
    }

    @Test
    void rejectsBlankDisplayName() {
        assertThatThrownBy(() -> useCase.execute(new RegisterUserCommand("ada@example.com", " ", "correct-horse")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsShortPassword() {
        assertThatThrownBy(() -> useCase.execute(new RegisterUserCommand("ada@example.com", "Ada", "short")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 8");
    }
}
