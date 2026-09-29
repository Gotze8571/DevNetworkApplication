package com.devnetwork.presentation.rest.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Replaces the account details. Omitted or blank optional fields are cleared. */
public record UpdateAccountRequest(
        @NotBlank @Size(max = 100) String displayName,
        @Size(max = 150) String headline,
        @Size(max = 2000) String bio,
        @Size(max = 100) String location,
        @Size(max = 500) String githubUrl,
        @Size(max = 500) String linkedinUrl,
        @Size(max = 500) String websiteUrl
) {
}
