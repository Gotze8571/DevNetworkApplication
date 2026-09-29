package com.devnetwork.application.account;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserNotFoundException;
import com.devnetwork.domain.user.UserRepository;

public class UpdateAccountUseCase {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UpdateAccountUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public Account execute(UpdateAccountCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        Profile current = profileRepository.findByUserId(user.getId()).orElseGet(() -> Profile.empty(user.getId()));

        // Build both first so a validation error in either leaves nothing half-saved.
        User renamed = user.rename(command.displayName());
        Profile updated = current.update(command.headline(), command.bio(), command.location(),
                command.githubUrl(), command.linkedinUrl(), command.websiteUrl());

        return new Account(userRepository.save(renamed), profileRepository.save(updated));
    }
}
