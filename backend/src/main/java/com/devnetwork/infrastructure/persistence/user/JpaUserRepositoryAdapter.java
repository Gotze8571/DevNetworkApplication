package com.devnetwork.infrastructure.persistence.user;

import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter: implements the domain's {@link UserRepository} port using Spring Data JPA.
 */
@Repository
class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpaRepository;

    JpaUserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(UserJpaEntity.fromDomain(user)).toDomain();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(UserJpaEntity::toDomain).toList();
    }
}
