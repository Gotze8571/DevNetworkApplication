package com.devnetwork.infrastructure.persistence.profile;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class JpaProfileRepositoryAdapter implements ProfileRepository {

    private final SpringDataProfileRepository jpaRepository;

    JpaProfileRepositoryAdapter(SpringDataProfileRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Profile save(Profile profile) {
        return jpaRepository.save(ProfileJpaEntity.fromDomain(profile)).toDomain();
    }

    @Override
    public Optional<Profile> findByUserId(UUID userId) {
        return jpaRepository.findById(userId).map(ProfileJpaEntity::toDomain);
    }

    @Override
    public List<Profile> findAllByUserIds(Collection<UUID> userIds) {
        return jpaRepository.findAllById(userIds).stream().map(ProfileJpaEntity::toDomain).toList();
    }
}
