package com.devnetwork.application.connection;

import com.devnetwork.application.member.MemberSummary;

import java.time.Instant;
import java.util.UUID;

/**
 * A connection from the point of view of one of its two users.
 */
public record ConnectionView(UUID id, MemberSummary member, State state, Instant createdAt, Instant respondedAt) {

    public enum State {
        /** Both users are connected. */
        CONNECTED,
        /** The other user sent a request the viewer has not answered yet. */
        INCOMING,
        /** The viewer sent a request the other user has not answered yet. */
        OUTGOING
    }
}
