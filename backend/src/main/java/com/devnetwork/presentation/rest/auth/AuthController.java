package com.devnetwork.presentation.rest.auth;

import com.devnetwork.application.auth.LoginCommand;
import com.devnetwork.application.auth.LoginUseCase;
import com.devnetwork.application.user.GetUserUseCase;
import com.devnetwork.application.user.RegisterUserCommand;
import com.devnetwork.application.user.RegisterUserUseCase;
import com.devnetwork.domain.user.User;
import com.devnetwork.infrastructure.security.SessionAuthenticator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Registration, login and the current session. Logout is {@code POST /api/auth/logout},
 * handled by Spring Security's logout filter (see SecurityConfig).
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Register, sign in and inspect the current session. "
        + "Sign out with POST /api/auth/logout.")
public class AuthController {

    private final RegisterUserUseCase registerUser;
    private final LoginUseCase login;
    private final GetUserUseCase getUser;
    private final SessionAuthenticator sessionAuthenticator;

    public AuthController(RegisterUserUseCase registerUser, LoginUseCase login, GetUserUseCase getUser,
                          SessionAuthenticator sessionAuthenticator) {
        this.registerUser = registerUser;
        this.login = login;
        this.getUser = getUser;
        this.sessionAuthenticator = sessionAuthenticator;
    }

    @Operation(summary = "Create an account and sign in")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CurrentUserResponse register(@Valid @RequestBody RegisterRequest body,
                                        HttpServletRequest request, HttpServletResponse response) {
        User user = registerUser.execute(new RegisterUserCommand(body.email(), body.displayName(), body.password()));
        sessionAuthenticator.signIn(user.getId(), request, response);
        return CurrentUserResponse.from(user);
    }

    @Operation(summary = "Sign in with email and password")
    @PostMapping("/login")
    public CurrentUserResponse login(@Valid @RequestBody LoginRequest body,
                                     HttpServletRequest request, HttpServletResponse response) {
        User user = login.execute(new LoginCommand(body.email(), body.password()));
        sessionAuthenticator.signIn(user.getId(), request, response);
        return CurrentUserResponse.from(user);
    }

    @Operation(summary = "The signed-in user (401 if there is no session)")
    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal UUID userId) {
        return CurrentUserResponse.from(getUser.execute(userId));
    }

    @Operation(summary = "Issue the XSRF-TOKEN cookie needed for POST, PUT and DELETE requests")
    @GetMapping("/csrf")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void csrf() {
        // The CSRF filter sets the cookie on every response; this endpoint just gives clients a cheap way to get it.
    }
}
