package com.devnetwork.domain.profile;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * The editable details a user shares about themselves. Every field is optional.
 */
public final class Profile {

    public static final int MAX_HEADLINE_LENGTH = 150;
    public static final int MAX_BIO_LENGTH = 2000;
    public static final int MAX_LOCATION_LENGTH = 100;
    public static final int MAX_URL_LENGTH = 500;

    private final UUID userId;
    private final String headline;
    private final String bio;
    private final String location;
    private final String githubUrl;
    private final String linkedinUrl;
    private final String websiteUrl;
    /** Null until the user saves their profile for the first time. */
    private final Instant updatedAt;

    private Profile(UUID userId, String headline, String bio, String location,
                    String githubUrl, String linkedinUrl, String websiteUrl, Instant updatedAt) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.headline = text(headline, MAX_HEADLINE_LENGTH, "Headline");
        this.bio = text(bio, MAX_BIO_LENGTH, "Bio");
        this.location = text(location, MAX_LOCATION_LENGTH, "Location");
        this.githubUrl = url(githubUrl, "GitHub URL");
        this.linkedinUrl = url(linkedinUrl, "LinkedIn URL");
        this.websiteUrl = url(websiteUrl, "Website URL");
        this.updatedAt = updatedAt;
    }

    /** The profile of a user who has not filled anything in yet. */
    public static Profile empty(UUID userId) {
        return new Profile(userId, null, null, null, null, null, null, null);
    }

    public static Profile restore(UUID userId, String headline, String bio, String location,
                                  String githubUrl, String linkedinUrl, String websiteUrl, Instant updatedAt) {
        return new Profile(userId, headline, bio, location, githubUrl, linkedinUrl, websiteUrl,
                Objects.requireNonNull(updatedAt, "updatedAt"));
    }

    /** Returns a copy with new details. Blank values clear a field. */
    public Profile update(String headline, String bio, String location,
                          String githubUrl, String linkedinUrl, String websiteUrl) {
        return new Profile(userId, headline, bio, location, githubUrl, linkedinUrl, websiteUrl, Instant.now());
    }

    private static String text(String value, int maxLength, String field) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be at most " + maxLength + " characters");
        }
        return trimmed;
    }

    private static String url(String value, String field) {
        String trimmed = text(value, MAX_URL_LENGTH, field);
        if (trimmed == null) {
            return null;
        }
        try {
            URI uri = new URI(trimmed);
            String scheme = uri.getScheme();
            if (("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) && uri.getHost() != null) {
                return trimmed;
            }
        } catch (URISyntaxException ignored) {
            // Fall through to the error below.
        }
        throw new IllegalArgumentException(field + " must be a valid http(s) link");
    }

    public UUID getUserId() {
        return userId;
    }

    public String getHeadline() {
        return headline;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
