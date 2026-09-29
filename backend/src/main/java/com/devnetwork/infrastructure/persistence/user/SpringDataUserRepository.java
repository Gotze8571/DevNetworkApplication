package com.devnetwork.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByEmail(String email);
}
