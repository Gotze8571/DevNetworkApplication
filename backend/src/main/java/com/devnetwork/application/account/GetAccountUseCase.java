package com.devnetwork.application.account;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserNotFoundException;
import com.devnetwork.domain.user.UserRepository;

import java.util.UUID;

public class GetAccountUseCase {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public GetAccountUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Account execute(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> Profile.empty(userId));
        return new Account(user, profile);
    }
}
