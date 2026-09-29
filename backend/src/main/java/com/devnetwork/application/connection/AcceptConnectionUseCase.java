package com.devnetwork.application.connection;

import com.devnetwork.application.member.MemberSummaries;
import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionNotFoundException;
import com.devnetwork.domain.connection.ConnectionRepository;

import java.util.List;
import java.util.UUID;

public class AcceptConnectionUseCase {

    private final ConnectionRepository connectionRepository;
    private final MemberSummaries memberSummaries;

    public AcceptConnectionUseCase(ConnectionRepository connectionRepository, MemberSummaries memberSummaries) {
        this.connectionRepository = connectionRepository;
        this.memberSummaries = memberSummaries;
    }

    public ConnectionView execute(UUID currentUserId, UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId)
                .filter(c -> c.involves(currentUserId))
                .orElseThrow(() -> new ConnectionNotFoundException(connectionId));

        Connection accepted = connectionRepository.save(connection.accept(currentUserId));
        UUID otherId = accepted.otherParty(currentUserId);
        return ListConnectionsUseCase.toView(accepted, currentUserId, memberSummaries.forIds(List.of(otherId)).get(otherId));
    }
}
