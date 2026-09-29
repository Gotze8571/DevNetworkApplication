package com.devnetwork.domain.profile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository {

    Profile save(Profile profile);

    Optional<Profile> findByUserId(UUID userId);

    List<Profile> findAllByUserIds(Collection<UUID> userIds);
}
