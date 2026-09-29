package com.devnetwork.application.account;

import java.util.UUID;

public record UpdateAccountCommand(
        UUID userId,
        String displayName,
        String headline,
        String bio,
        String location,
        String githubUrl,
        String linkedinUrl,
        String websiteUrl
) {
}
