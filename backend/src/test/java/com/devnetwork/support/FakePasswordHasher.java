package com.devnetwork.support;

import com.devnetwork.application.auth.PasswordHasher;

/** Reversible "hash" so tests run instantly; the real one is BCrypt. */
public class FakePasswordHasher implements PasswordHasher {

    @Override
    public String hash(String rawPassword) {
        return "hashed:" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return passwordHash.equals(hash(rawPassword));
    }
}
