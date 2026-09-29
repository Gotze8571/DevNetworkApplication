package com.devnetwork.application.connection;

import com.devnetwork.domain.connection.ConnectionNotFoundException;
import com.devnetwork.domain.connection.ConnectionRepository;

import java.util.UUID;

/**
 * Declines an incoming request, cancels an outgoing one, or removes an existing connection.
 * Either user in the connection may do this.
 */
public class RemoveConnectionUseCase {

    private final ConnectionRepository connectionRepository;

    public RemoveConnectionUseCase(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public void execute(UUID currentUserId, UUID connectionId) {
        connectionRepository.findById(connectionId)
                .filter(c -> c.involves(currentUserId))
                .orElseThrow(() -> new ConnectionNotFoundException(connectionId));
        connectionRepository.delete(connectionId);
    }
}
