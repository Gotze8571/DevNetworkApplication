package com.devnetwork.application.member;

import com.devnetwork.domain.profile.Profile;

import java.time.Instant;
import java.util.UUID;

/**
 * A member's public profile, as seen by other users.
 */
public record MemberProfile(UUID id, String displayName, Instant joinedAt, Profile profile) {
}
