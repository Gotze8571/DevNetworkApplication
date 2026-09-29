package com.devnetwork.presentation.rest.member;

import com.devnetwork.application.member.MemberProfile;

import java.time.Instant;
import java.util.UUID;

/** A member's public profile. Excludes the email address. */
public record MemberProfileResponse(
        UUID id,
        String displayName,
        Instant joinedAt,
        String headline,
        String bio,
        String location,
        String githubUrl,
        String linkedinUrl,
        String websiteUrl
) {

    static MemberProfileResponse from(MemberProfile member) {
        var profile = member.profile();
        return new MemberProfileResponse(member.id(), member.displayName(), member.joinedAt(),
                profile.getHeadline(), profile.getBio(), profile.getLocation(),
                profile.getGithubUrl(), profile.getLinkedinUrl(), profile.getWebsiteUrl());
    }
}
