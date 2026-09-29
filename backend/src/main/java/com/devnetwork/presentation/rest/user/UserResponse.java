package com.devnetwork.presentation.rest.user;

import com.devnetwork.domain.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String email, String displayName, Instant createdAt) {

    static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getCreatedAt());
    }
}
