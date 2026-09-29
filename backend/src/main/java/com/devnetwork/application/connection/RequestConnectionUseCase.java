package com.devnetwork.application.connection;

import com.devnetwork.application.member.MemberSummaries;
import com.devnetwork.domain.connection.Connection;
import com.devnetwork.domain.connection.ConnectionAlreadyExistsException;
import com.devnetwork.domain.connection.ConnectionRepository;
import com.devnetwork.domain.user.UserNotFoundException;
import com.devnetwork.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Sends a connection request. If the other user already sent one to the requester, it is accepted instead.
 */
public class RequestConnectionUseCase {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;
    private final MemberSummaries memberSummaries;

    public RequestConnectionUseCase(ConnectionRepository connectionRepository, UserRepository userRepository,
                                    MemberSummaries memberSummaries) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.memberSummaries = memberSummaries;
    }

    public ConnectionView execute(UUID requesterId, UUID addresseeId) {
        if (userRepository.findById(addresseeId).isEmpty()) {
            throw new UserNotFoundException(addresseeId);
        }

        Optional<Connection> existing = connectionRepository.findBetween(requesterId, addresseeId);
        Connection saved;
        if (existing.isEmpty()) {
            saved = connectionRepository.save(Connection.request(requesterId, addresseeId));
        } else if (existing.get().isPending() && existing.get().getAddresseeId().equals(requesterId)) {
            saved = connectionRepository.save(existing.get().accept(requesterId));
        } else {
            throw new ConnectionAlreadyExistsException();
        }

        return ListConnectionsUseCase.toView(saved, requesterId, memberSummaries.forIds(List.of(addresseeId)).get(addresseeId));
    }
}
