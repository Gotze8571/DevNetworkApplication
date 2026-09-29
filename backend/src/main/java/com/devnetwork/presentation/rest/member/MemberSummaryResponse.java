package com.devnetwork.presentation.rest.member;

import com.devnetwork.application.member.MemberSummary;

import java.util.UUID;

public record MemberSummaryResponse(UUID id, String displayName, String headline) {

    public static MemberSummaryResponse from(MemberSummary summary) {
        return new MemberSummaryResponse(summary.id(), summary.displayName(), summary.headline());
    }
}
