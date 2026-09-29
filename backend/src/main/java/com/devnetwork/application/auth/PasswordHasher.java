package com.devnetwork.application.auth;

/**
 * Port for one-way password hashing, implemented in infrastructure (BCrypt).
 */
public interface PasswordHasher {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String passwordHash);
}
