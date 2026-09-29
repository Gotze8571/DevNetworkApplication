package com.devnetwork.presentation.rest.member;

import com.devnetwork.application.member.GetMemberUseCase;
import com.devnetwork.application.member.ListMembersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Other users' public profiles")
public class MemberController {

    private final ListMembersUseCase listMembers;
    private final GetMemberUseCase getMember;

    public MemberController(ListMembersUseCase listMembers, GetMemberUseCase getMember) {
        this.listMembers = listMembers;
        this.getMember = getMember;
    }

    @Operation(summary = "List every member except me")
    @GetMapping
    public List<MemberSummaryResponse> list(@AuthenticationPrincipal UUID userId) {
        return listMembers.execute(userId).stream().map(MemberSummaryResponse::from).toList();
    }

    @Operation(summary = "Get a member's public profile")
    @GetMapping("/{id}")
    public MemberProfileResponse get(@PathVariable UUID id) {
        return MemberProfileResponse.from(getMember.execute(id));
    }
}
