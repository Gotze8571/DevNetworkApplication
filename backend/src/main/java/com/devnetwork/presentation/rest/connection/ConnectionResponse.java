package com.devnetwork.presentation.rest.connection;

import com.devnetwork.application.connection.ConnectionView;
import com.devnetwork.presentation.rest.member.MemberSummaryResponse;

import java.time.Instant;
import java.util.UUID;

/**
 * A connection as seen by the signed-in user. {@code state} is CONNECTED, INCOMING (awaiting my answer)
 * or OUTGOING (awaiting theirs).
 */
public record ConnectionResponse(
        UUID id,
        MemberSummaryResponse member,
        ConnectionView.State state,
        Instant createdAt,
        Instant respondedAt
) {

    static ConnectionResponse from(ConnectionView view) {
        return new ConnectionResponse(view.id(), MemberSummaryResponse.from(view.member()), view.state(),
                view.createdAt(), view.respondedAt());
    }
}
