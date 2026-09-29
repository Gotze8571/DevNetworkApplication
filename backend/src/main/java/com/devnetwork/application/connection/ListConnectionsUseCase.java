package com.devnetwork.application.connection;

import com.devnetwork.application.member.MemberSummaries;
import com.devnetwork.application.member.MemberSummary;
import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListConnectionsUseCase {

    private final ConnectionRepository connectionRepository;
    private final MemberSummaries memberSummaries;

    public ListConnectionsUseCase(ConnectionRepository connectionRepository, MemberSummaries memberSummaries) {
        this.connectionRepository = connectionRepository;
        this.memberSummaries = memberSummaries;
    }

    public List<ConnectionView> execute(UUID currentUserId) {
        List<Connection> connections = connectionRepository.findAllInvolving(currentUserId);
        Map<UUID, MemberSummary> members = memberSummaries.forIds(
                connections.stream().map(c -> c.otherParty(currentUserId)).toList());

        return connections.stream()
                .sorted(Comparator.comparing(Connection::getCreatedAt).reversed())
                .map(c -> toView(c, currentUserId, members.get(c.otherParty(currentUserId))))
                .toList();
    }

    static ConnectionView toView(Connection connection, UUID viewerId, MemberSummary other) {
        ConnectionView.State state;
        if (!connection.isPending()) {
            state = ConnectionView.State.CONNECTED;
        } else if (connection.getAddresseeId().equals(viewerId)) {
            state = ConnectionView.State.INCOMING;
        } else {
            state = ConnectionView.State.OUTGOING;
        }
        return new ConnectionView(connection.getId(), other, state, connection.getCreatedAt(), connection.getRespondedAt());
    }
}
