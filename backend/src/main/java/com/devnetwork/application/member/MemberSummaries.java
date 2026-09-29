package com.devnetwork.application.member;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builds {@link MemberSummary} views by combining users with their profiles in two queries.
 */
public class MemberSummaries {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public MemberSummaries(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Map<UUID, MemberSummary> forIds(Collection<UUID> userIds) {
        if (userIds.isEmpty()) {
            return Map.of();
        }
        return summarize(userRepository.findAllById(userIds));
    }

    public Map<UUID, MemberSummary> summarize(List<User> users) {
        Map<UUID, Profile> profiles = profileRepository.findAllByUserIds(users.stream().map(User::getId).toList())
                .stream()
                .collect(Collectors.toMap(Profile::getUserId, Function.identity()));

        Map<UUID, MemberSummary> summaries = new LinkedHashMap<>();
        for (User user : users) {
            Profile profile = profiles.get(user.getId());
            String headline = profile == null ? null : profile.getHeadline();
            summaries.put(user.getId(), new MemberSummary(user.getId(), user.getDisplayName(), headline));
        }
        return summaries;
    }
}
