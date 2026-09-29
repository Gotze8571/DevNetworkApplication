package com.devnetwork.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Starts an HTTP session for a user whose credentials have already been checked.
 * The session cookie then identifies them on later requests; the principal is their user id.
 */
@Component
public class SessionAuthenticator {

    private final SecurityContextRepository securityContextRepository;

    SessionAuthenticator(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    public void signIn(UUID userId, HttpServletRequest request, HttpServletResponse response) {
        // Issue a new session id on login so a session id planted before login can't be reused (session fixation).
        HttpSession existing = request.getSession(false);
        if (existing != null) {
            request.changeSessionId();
        }

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                userId, null, AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
