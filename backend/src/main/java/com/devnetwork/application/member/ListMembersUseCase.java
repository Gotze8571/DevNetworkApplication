package com.devnetwork.application.member;

import com.devnetwork.domain.user.User;
import com.devnetwork.domain.user.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * The member directory: everyone except the user who is looking.
 */
public class ListMembersUseCase {

    private final UserRepository userRepository;
    private final MemberSummaries memberSummaries;

    public ListMembersUseCase(UserRepository userRepository, MemberSummaries memberSummaries) {
        this.userRepository = userRepository;
        this.memberSummaries = memberSummaries;
    }

    public List<MemberSummary> execute(UUID currentUserId) {
        List<User> others = userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .toList();
        return memberSummaries.summarize(others).values().stream()
                .sorted(Comparator.comparing(MemberSummary::displayName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
