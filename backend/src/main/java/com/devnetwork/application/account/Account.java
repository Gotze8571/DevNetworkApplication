package com.devnetwork.application.account;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.user.User;

/**
 * Everything a signed-in user can see and edit about themselves.
 */
public record Account(User user, Profile profile) {
}
