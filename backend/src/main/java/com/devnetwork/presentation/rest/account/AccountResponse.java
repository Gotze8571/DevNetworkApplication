package com.devnetwork.presentation.rest.account;

import com.devnetwork.application.account.Account;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String email,
        String displayName,
        Instant createdAt,
        String headline,
        String bio,
        String location,
        String githubUrl,
        String linkedinUrl,
        String websiteUrl,
        Instant updatedAt
) {

    static AccountResponse from(Account account) {
        var user = account.user();
        var profile = account.profile();
        return new AccountResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getCreatedAt(),
                profile.getHeadline(), profile.getBio(), profile.getLocation(),
                profile.getGithubUrl(), profile.getLinkedinUrl(), profile.getWebsiteUrl(), profile.getUpdatedAt());
    }
}
