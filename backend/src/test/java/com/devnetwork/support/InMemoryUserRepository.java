package com.devnetwork.support;

import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> users = new LinkedHashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public List<User> findAllById(Collection<UUID> ids) {
        return users.values().stream().filter(u -> ids.contains(u.getId())).toList();
    }
}
