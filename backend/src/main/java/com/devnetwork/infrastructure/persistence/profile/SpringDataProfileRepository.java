package com.devnetwork.infrastructure.persistence.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SpringDataProfileRepository extends JpaRepository<ProfileJpaEntity, UUID> {
}
