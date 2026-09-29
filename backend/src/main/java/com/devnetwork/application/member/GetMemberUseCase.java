package com.devnetwork.application.member;

import com.devnetwork.domain.profile.Profile;
import com.devnetwork.domain.profile.ProfileRepository;
import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserNotFoundException;
import com.devnetwork.domain.user.UserRepository;

import java.util.UUID;

public class GetMemberUseCase {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public GetMemberUseCase(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public MemberProfile execute(UUID memberId) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException(memberId));
        Profile profile = profileRepository.findByUserId(memberId).orElseGet(() -> Profile.empty(memberId));
        return new MemberProfile(user.getId(), user.getDisplayName(), user.getCreatedAt(), profile);
    }
}
