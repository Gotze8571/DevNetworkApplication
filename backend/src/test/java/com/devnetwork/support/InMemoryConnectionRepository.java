package com.devnetwork.support;

import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryConnectionRepository implements ConnectionRepository {

    private final Map<UUID, Connection> connections = new LinkedHashMap<>();

    @Override
    public Connection save(Connection connection) {
        connections.put(connection.getId(), connection);
        return connection;
    }

    @Override
    public Optional<Connection> findById(UUID id) {
        return Optional.ofNullable(connections.get(id));
    }

    @Override
    public Optional<Connection> findBetween(UUID userId, UUID otherUserId) {
        return connections.values().stream()
                .filter(c -> c.involves(userId) && c.involves(otherUserId))
                .findFirst();
    }

    @Override
    public List<Connection> findAllInvolving(UUID userId) {
        return connections.values().stream().filter(c -> c.involves(userId)).toList();
    }

    @Override
    public void delete(UUID id) {
        connections.remove(id);
    }
}
