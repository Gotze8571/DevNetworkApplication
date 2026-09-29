package com.devnetwork.domain.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port: the domain declares what it needs; infrastructure provides the implementation.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);

    List<User> findAll();
}
