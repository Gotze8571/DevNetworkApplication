package com.devnetwork.support;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProfileRepository implements ProfileRepository {

    private final Map<UUID, Profile> profiles = new HashMap<>();

    @Override
    public Profile save(Profile profile) {
        profiles.put(profile.getUserId(), profile);
        return profile;
    }

    @Override
    public Optional<Profile> findByUserId(UUID userId) {
        return Optional.ofNullable(profiles.get(userId));
    }

    @Override
    public List<Profile> findAllByUserIds(Collection<UUID> userIds) {
        return profiles.values().stream().filter(p -> userIds.contains(p.getUserId())).toList();
    }
}
