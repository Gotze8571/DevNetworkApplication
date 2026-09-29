package com.devnetwork.infrastructure.persistence.user;

import com.devnetwork.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Persistence model. Kept separate from the domain {@link User} so JPA concerns stay out of the domain.
 */
@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected UserJpaEntity() {
    }

    static UserJpaEntity fromDomain(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.id = user.getId();
        entity.email = user.getEmail();
        entity.displayName = user.getDisplayName();
        entity.passwordHash = user.getPasswordHash();
        entity.createdAt = user.getCreatedAt();
        return entity;
    }

    User toDomain() {
        return User.restore(id, email, displayName, passwordHash, createdAt);
    }
}
