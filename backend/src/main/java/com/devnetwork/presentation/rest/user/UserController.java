package com.devnetwork.presentation.rest.user;

import com.devnetwork.application.user.GetUserUseCase;
import com.devnetwork.application.user.ListUsersUseCase;
import com.devnetwork.application.user.RegisterUserCommand;
import com.devnetwork.application.user.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUserUseCase registerUser;
    private final GetUserUseCase getUser;
    private final ListUsersUseCase listUsers;

    public UserController(RegisterUserUseCase registerUser, GetUserUseCase getUser, ListUsersUseCase listUsers) {
        this.registerUser = registerUser;
        this.getUser = getUser;
        this.listUsers = listUsers;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        return UserResponse.from(registerUser.execute(new RegisterUserCommand(request.email(), request.displayName())));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return UserResponse.from(getUser.execute(id));
    }

    @GetMapping
    public List<UserResponse> list() {
        return listUsers.execute().stream().map(UserResponse::from).toList();
    }
}
