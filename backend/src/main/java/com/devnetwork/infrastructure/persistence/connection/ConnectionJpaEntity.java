package com.devnetwork.infrastructure.persistence.connection;

import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "connections")
public class ConnectionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;

    @Column(name = "addressee_id", nullable = false)
    private UUID addresseeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConnectionStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    protected ConnectionJpaEntity() {
    }

    static ConnectionJpaEntity fromDomain(Connection connection) {
        ConnectionJpaEntity entity = new ConnectionJpaEntity();
        entity.id = connection.getId();
        entity.requesterId = connection.getRequesterId();
        entity.addresseeId = connection.getAddresseeId();
        entity.status = connection.getStatus();
        entity.createdAt = connection.getCreatedAt();
        entity.respondedAt = connection.getRespondedAt();
        return entity;
    }

    Connection toDomain() {
        return Connection.restore(id, requesterId, addresseeId, status, createdAt, respondedAt);
    }
}
