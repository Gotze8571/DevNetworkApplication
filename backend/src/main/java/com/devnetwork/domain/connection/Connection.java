package com.devnetwork.domain.connection;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A connection between two users. It starts as a request from the requester and becomes
 * mutual once the addressee accepts. Declined, cancelled and removed connections are deleted.
 */
public final class Connection {

    private final UUID id;
    private final UUID requesterId;
    private final UUID addresseeId;
    private final ConnectionStatus status;
    private final Instant createdAt;
    private final Instant respondedAt;

    private Connection(UUID id, UUID requesterId, UUID addresseeId, ConnectionStatus status,
                       Instant createdAt, Instant respondedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.requesterId = Objects.requireNonNull(requesterId, "requesterId");
        this.addresseeId = Objects.requireNonNull(addresseeId, "addresseeId");
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.respondedAt = respondedAt;
        if (requesterId.equals(addresseeId)) {
            throw new IllegalArgumentException("You cannot connect with yourself");
        }
    }

    public static Connection request(UUID requesterId, UUID addresseeId) {
        return new Connection(UUID.randomUUID(), requesterId, addresseeId, ConnectionStatus.PENDING, Instant.now(), null);
    }

    public static Connection restore(UUID id, UUID requesterId, UUID addresseeId, ConnectionStatus status,
                                     Instant createdAt, Instant respondedAt) {
        return new Connection(id, requesterId, addresseeId, status, createdAt, respondedAt);
    }

    /** Accepts a pending request. Only the user it was sent to may accept it. */
    public Connection accept(UUID userId) {
        if (status != ConnectionStatus.PENDING) {
            throw new ConnectionActionNotAllowedException("This connection request is no longer pending");
        }
        if (!addresseeId.equals(userId)) {
            throw new ConnectionActionNotAllowedException("Only the recipient can accept a connection request");
        }
        return new Connection(id, requesterId, addresseeId, ConnectionStatus.ACCEPTED, createdAt, Instant.now());
    }

    public boolean involves(UUID userId) {
        return requesterId.equals(userId) || addresseeId.equals(userId);
    }

    /** The user on the other side of this connection from {@code userId}. */
    public UUID otherParty(UUID userId) {
        if (requesterId.equals(userId)) {
            return addresseeId;
        }
        if (addresseeId.equals(userId)) {
            return requesterId;
        }
        throw new IllegalArgumentException("User is not part of this connection");
    }

    public boolean isPending() {
        return status == ConnectionStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequesterId() {
        return requesterId;
    }

    public UUID getAddresseeId() {
        return addresseeId;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }
}
