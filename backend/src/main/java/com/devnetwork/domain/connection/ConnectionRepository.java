package com.devnetwork.domain.connection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConnectionRepository {

    Connection save(Connection connection);

    Optional<Connection> findById(UUID id);

    /** The connection between two users, whichever of them sent the request. */
    Optional<Connection> findBetween(UUID userId, UUID otherUserId);

    List<Connection> findAllInvolving(UUID userId);

    void delete(UUID id);
}
