package com.devnetwork.presentation.rest.account;

import com.devnetwork.application.account.GetAccountUseCase;
import com.devnetwork.application.account.UpdateAccountCommand;
import com.devnetwork.application.account.UpdateAccountUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "The signed-in user's own details")
public class AccountController {

    private final GetAccountUseCase getAccount;
    private final UpdateAccountUseCase updateAccount;

    public AccountController(GetAccountUseCase getAccount, UpdateAccountUseCase updateAccount) {
        this.getAccount = getAccount;
        this.updateAccount = updateAccount;
    }

    @Operation(summary = "Get my account and profile")
    @GetMapping
    public AccountResponse get(@AuthenticationPrincipal UUID userId) {
        return AccountResponse.from(getAccount.execute(userId));
    }

    @Operation(summary = "Update my display name and profile")
    @PutMapping
    public AccountResponse update(@AuthenticationPrincipal UUID userId, @Valid @RequestBody UpdateAccountRequest body) {
        return AccountResponse.from(updateAccount.execute(new UpdateAccountCommand(userId, body.displayName(),
                body.headline(), body.bio(), body.location(), body.githubUrl(), body.linkedinUrl(), body.websiteUrl())));
    }
}
