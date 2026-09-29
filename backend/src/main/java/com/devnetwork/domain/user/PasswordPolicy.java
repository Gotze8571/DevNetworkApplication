package com.devnetwork.domain.user;

import java.nio.charset.StandardCharsets;

/**
 * Rules a plain-text password must meet before it is hashed.
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 8;
    /** BCrypt only uses the first 72 bytes, so longer passwords are rejected rather than silently truncated. */
    public static final int MAX_BYTES = 72;

    private PasswordPolicy() {
    }

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters");
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES) {
            throw new IllegalArgumentException("Password must be at most " + MAX_BYTES + " bytes");
        }
    }
}
