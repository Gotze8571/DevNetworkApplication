package com.devnetwork.application.member;

import java.util.UUID;

/**
 * What other users can see about a member in lists. Deliberately excludes the email address.
 */
public record MemberSummary(UUID id, String displayName, String headline) {
}
