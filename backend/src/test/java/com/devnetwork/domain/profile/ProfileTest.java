package com.devnetwork.domain.profile;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    private final Profile empty = Profile.empty(UUID.randomUUID());

    @Test
    void trimsValuesAndTurnsBlankIntoNull() {
        Profile profile = empty.update("  Backend engineer ", "   ", null, "https://github.com/ada", null, null);

        assertThat(profile.getHeadline()).isEqualTo("Backend engineer");
        assertThat(profile.getBio()).isNull();
        assertThat(profile.getGithubUrl()).isEqualTo("https://github.com/ada");
        assertThat(profile.getUpdatedAt()).isNotNull();
    }

    @Test
    void rejectsNonHttpLinks() {
        assertThatThrownBy(() -> empty.update(null, null, null, "javascript:alert(1)", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GitHub URL");
    }

    @Test
    void rejectsTooLongHeadline() {
        assertThatThrownBy(() -> empty.update("x".repeat(151), null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
