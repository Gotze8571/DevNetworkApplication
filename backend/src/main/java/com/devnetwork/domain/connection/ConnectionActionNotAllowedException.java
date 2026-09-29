package com.devnetwork.domain.connection;

public class ConnectionActionNotAllowedException extends RuntimeException {

    public ConnectionActionNotAllowedException(String message) {
        super(message);
    }
}
