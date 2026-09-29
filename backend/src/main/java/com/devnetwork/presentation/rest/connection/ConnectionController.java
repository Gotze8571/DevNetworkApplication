package com.devnetwork.presentation.rest.connection;

import com.devnetwork.application.connection.AcceptConnectionUseCase;
import com.devnetwork.application.connection.ListConnectionsUseCase;
import com.devnetwork.application.connection.RemoveConnectionUseCase;
import com.devnetwork.application.connection.RequestConnectionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/connections")
@Tag(name = "Connections", description = "Connection requests and connections between members")
public class ConnectionController {

    private final ListConnectionsUseCase listConnections;
    private final RequestConnectionUseCase requestConnection;
    private final AcceptConnectionUseCase acceptConnection;
    private final RemoveConnectionUseCase removeConnection;

    public ConnectionController(ListConnectionsUseCase listConnections, RequestConnectionUseCase requestConnection,
                                AcceptConnectionUseCase acceptConnection, RemoveConnectionUseCase removeConnection) {
        this.listConnections = listConnections;
        this.requestConnection = requestConnection;
        this.acceptConnection = acceptConnection;
        this.removeConnection = removeConnection;
    }

    @Operation(summary = "My connections and pending requests, newest first")
    @GetMapping
    public List<ConnectionResponse> list(@AuthenticationPrincipal UUID userId) {
        return listConnections.execute(userId).stream().map(ConnectionResponse::from).toList();
    }

    @Operation(summary = "Send a connection request (accepts theirs if they already sent me one)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionResponse request(@AuthenticationPrincipal UUID userId, @Valid @RequestBody ConnectionRequest body) {
        return ConnectionResponse.from(requestConnection.execute(userId, body.userId()));
    }

    @Operation(summary = "Accept a connection request sent to me")
    @PostMapping("/{id}/accept")
    public ConnectionResponse accept(@AuthenticationPrincipal UUID userId, @PathVariable UUID id) {
        return ConnectionResponse.from(acceptConnection.execute(userId, id));
    }

    @Operation(summary = "Decline, cancel or remove a connection")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@AuthenticationPrincipal UUID userId, @PathVariable UUID id) {
        removeConnection.execute(userId, id);
    }
}
