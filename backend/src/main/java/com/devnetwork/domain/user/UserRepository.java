package com.devnetwork.domain.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port: the domain declares what it needs; infrastructure provides the implementation.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAll();

    List<User> findAllById(Collection<UUID> ids);
}
