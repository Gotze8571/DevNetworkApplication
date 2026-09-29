package com.devnetwork.domain.connection;

public class ConnectionAlreadyExistsException extends RuntimeException {

    public ConnectionAlreadyExistsException() {
        super("You are already connected with, or have a pending request to, this user");
    }
}
