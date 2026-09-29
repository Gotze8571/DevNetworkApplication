package com.devnetwork.domain.user;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity. Pure Java: no Spring, JPA or HTTP annotations belong here.
 */
public final class User {

    private static final int MAX_DISPLAY_NAME_LENGTH = 100;

    private final UUID id;
    private final String email;
    private final String displayName;
    private final Instant createdAt;

    private User(UUID id, String email, String displayName, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.email = normalizeEmail(email);
        this.displayName = validateDisplayName(displayName);
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    /** Creates a brand-new user. */
    public static User register(String email, String displayName) {
        return new User(UUID.randomUUID(), email, displayName, Instant.now());
    }

    /** Rebuilds an existing user, e.g. when loading from persistence. */
    public static User restore(UUID id, String email, String displayName, Instant createdAt) {
        return new User(id, email, displayName, createdAt);
    }

    private static String normalizeEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("A valid email is required");
        }
        return email.trim().toLowerCase();
    }

    private static String validateDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Display name is required");
        }
        String trimmed = displayName.trim();
        if (trimmed.length() > MAX_DISPLAY_NAME_LENGTH) {
            throw new IllegalArgumentException("Display name must be at most " + MAX_DISPLAY_NAME_LENGTH + " characters");
        }
        return trimmed;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
