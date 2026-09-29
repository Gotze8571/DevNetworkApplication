package com.devnetwork.infrastructure.persistence.profile;

import com.devnetwork.domain.profile.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "profiles")
public class ProfileJpaEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(length = 150)
    private String headline;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(length = 100)
    private String location;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProfileJpaEntity() {
    }

    static ProfileJpaEntity fromDomain(Profile profile) {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.userId = profile.getUserId();
        entity.headline = profile.getHeadline();
        entity.bio = profile.getBio();
        entity.location = profile.getLocation();
        entity.githubUrl = profile.getGithubUrl();
        entity.linkedinUrl = profile.getLinkedinUrl();
        entity.websiteUrl = profile.getWebsiteUrl();
        entity.updatedAt = profile.getUpdatedAt();
        return entity;
    }

    Profile toDomain() {
        return Profile.restore(userId, headline, bio, location, githubUrl, linkedinUrl, websiteUrl, updatedAt);
    }
}
