package com.devnetwork.application.auth;

import com.devnetwork.application.user.RegisterUserCommand;
import com.devnetwork.application.user.RegisterUserUseCase;
import com.devnetwork.domain.user.User;
import com.devnetwork.support.FakePasswordHasher;
import com.devnetwork.support.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginUseCaseTest {

    private final InMemoryUserRepository repository = new InMemoryUserRepository();
    private final FakePasswordHasher hasher = new FakePasswordHasher();
    private final LoginUseCase login = new LoginUseCase(repository, hasher);
    private User ada;

    @BeforeEach
    void registerAda() {
        ada = new RegisterUserUseCase(repository, hasher)
                .execute(new RegisterUserCommand("ada@example.com", "Ada", "correct-horse"));
    }

    @Test
    void returnsUserForCorrectCredentialsIgnoringEmailCase() {
        assertThat(login.execute(new LoginCommand(" ADA@example.com", "correct-horse")).getId()).isEqualTo(ada.getId());
    }

    @Test
    void rejectsWrongPassword() {
        assertThatThrownBy(() -> login.execute(new LoginCommand("ada@example.com", "wrong-password")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void rejectsUnknownEmailWithSameError() {
        assertThatThrownBy(() -> login.execute(new LoginCommand("nobody@example.com", "correct-horse")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
