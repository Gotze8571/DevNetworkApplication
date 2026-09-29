package com.devnetwork.infrastructure.persistence.connection;

import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class JpaConnectionRepositoryAdapter implements ConnectionRepository {

    private final SpringDataConnectionRepository jpaRepository;

    JpaConnectionRepositoryAdapter(SpringDataConnectionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Connection save(Connection connection) {
        return jpaRepository.save(ConnectionJpaEntity.fromDomain(connection)).toDomain();
    }

    @Override
    public Optional<Connection> findById(UUID id) {
        return jpaRepository.findById(id).map(ConnectionJpaEntity::toDomain);
    }

    @Override
    public Optional<Connection> findBetween(UUID userId, UUID otherUserId) {
        return jpaRepository.findBetween(userId, otherUserId).map(ConnectionJpaEntity::toDomain);
    }

    @Override
    public List<Connection> findAllInvolving(UUID userId) {
        return jpaRepository.findAllInvolving(userId).stream().map(ConnectionJpaEntity::toDomain).toList();
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
