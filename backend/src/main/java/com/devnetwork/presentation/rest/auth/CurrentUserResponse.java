package com.devnetwork.presentation.rest.auth;

import com.devnetwork.domain.user.User;

import java.time.Instant;
import java.util.UUID;

/** The signed-in user. Only ever returned to that user, so it may include their email. */
public record CurrentUserResponse(UUID id, String email, String displayName, Instant createdAt) {

    static CurrentUserResponse from(User user) {
        return new CurrentUserResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getCreatedAt());
    }
}
