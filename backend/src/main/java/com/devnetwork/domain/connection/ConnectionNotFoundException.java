package com.devnetwork.domain.connection;

import java.util.UUID;

public class ConnectionNotFoundException extends RuntimeException {

    public ConnectionNotFoundException(UUID id) {
        super("Connection not found: " + id);
    }
}
