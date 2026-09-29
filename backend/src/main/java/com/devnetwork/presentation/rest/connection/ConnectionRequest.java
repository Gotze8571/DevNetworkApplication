package com.devnetwork.presentation.rest.connection;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConnectionRequest(@NotNull UUID userId) {
}
