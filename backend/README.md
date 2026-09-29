# Backend — Java 21 + Spring Boot 3

REST API for DevNetwork, organised with Clean Architecture.

## Package layout

```
src/main/java/com/devnetwork/
├── DevNetworkApplication.java
├── domain/                     # Entities, business rules, repository ports. No Spring/JPA.
│   └── user/                   #   User, UserRepository, domain exceptions
├── application/                # Use cases. Depend only on domain. No Spring annotations.
│   └── user/                   #   RegisterUserUseCase, GetUserUseCase, ListUsersUseCase
├── infrastructure/             # Framework and I/O details
│   ├── config/                 #   UseCaseConfig: wires use cases as Spring beans
│   └── persistence/user/       #   JPA entity + adapter implementing UserRepository
└── presentation/
    └── rest/                   # Controllers, request/response DTOs, exception → HTTP mapping
        └── user/
```

**Dependency rule:** `presentation` and `infrastructure` → `application` → `domain`. The domain never imports from another layer.

### Adding a feature (e.g. `connection`)

1. `domain/connection/` — entity, `ConnectionRepository` interface, exceptions.
2. `application/connection/` — one class per use case, taking the repository in its constructor.
3. `infrastructure/persistence/connection/` — JPA entity, Spring Data repo, adapter implementing the port.
4. Register the use cases in `infrastructure/config/UseCaseConfig`.
5. `presentation/rest/connection/` — controller + request/response records.
6. Add a Flyway migration in `src/main/resources/db/migration`.

## Running locally

Requires JDK 21 and Maven, plus a running database (`docker compose up database` from the repo root).

```bash
mvn spring-boot:run
mvn test
```

Configuration is read from environment variables (defaults in `application.yml`):

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/devnetwork` |
| `DB_USERNAME` | `devnetwork` |
| `DB_PASSWORD` | `devnetwork` |
| `PORT` | `8080` |

## API

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/users` | Register a user `{ "email", "displayName" }` |
| `GET` | `/api/users` | List users |
| `GET` | `/api/users/{id}` | Get a user |
| `GET` | `/actuator/health` | Health check |
