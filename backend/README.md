# Backend — Java 21 + Spring Boot 3

REST API for DevNetwork, organised with Clean Architecture.

## Package layout

```
src/main/java/com/devnetwork/
├── DevNetworkApplication.java
├── domain/                     # Entities, business rules, repository ports. No Spring/JPA.
│   └── user/ profile/ connection/
├── application/                # Use cases. Depend only on domain. No Spring annotations.
│   └── auth/ user/ account/ member/ connection/
├── infrastructure/             # Framework and I/O details
│   ├── config/                 #   UseCaseConfig: wires use cases as Spring beans
│   ├── security/               #   Spring Security config, BCrypt hasher, session sign-in
│   └── persistence/            #   JPA entities + adapters implementing the repository ports
└── presentation/
    └── rest/                   # Controllers, request/response DTOs, exception → HTTP mapping
        └── auth/ account/ member/ connection/
```

**Dependency rule:** `presentation` and `infrastructure` → `application` → `domain`. The domain never imports from another layer.

### Adding a feature (e.g. `post`)

1. `domain/post/` — entity, `PostRepository` interface, exceptions.
2. `application/post/` — one class per use case, taking the repository in its constructor.
3. `infrastructure/persistence/post/` — JPA entity, Spring Data repo, adapter implementing the port.
4. Register the use cases in `infrastructure/config/UseCaseConfig`.
5. `presentation/rest/post/` — controller + request/response records.
6. Add a Flyway migration in `src/main/resources/db/migration`.

## Running locally

Requires JDK 21 and Maven (`brew install openjdk@21 maven`, with `JAVA_HOME=/opt/homebrew/opt/openjdk@21`), plus a running database (`docker compose up database` from the repo root).

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
| `SESSION_TIMEOUT` | `30m` |
| `SESSION_COOKIE_SECURE` | `false` |

## API

Interactive docs (Swagger UI): http://localhost:8080/swagger-ui.html · OpenAPI spec: `/v3/api-docs`.

Everything under `/api` requires a signed-in session except register, login and csrf.

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Create an account `{ email, displayName, password }` and sign in |
| `POST` | `/api/auth/login` | Sign in `{ email, password }` |
| `POST` | `/api/auth/logout` | Sign out (ends the session) |
| `GET` | `/api/auth/me` | The signed-in user, or `401` |
| `GET` | `/api/auth/csrf` | Sets the `XSRF-TOKEN` cookie |
| `GET` | `/api/account` | My email, display name and profile |
| `PUT` | `/api/account` | Update my display name and profile |
| `GET` | `/api/members` | Everyone except me (no emails) |
| `GET` | `/api/members/{id}` | A member's public profile |
| `GET` | `/api/connections` | My connections and requests, each `CONNECTED`, `INCOMING` or `OUTGOING` |
| `POST` | `/api/connections` | Send a request `{ userId }`; accepts theirs if they already asked me |
| `POST` | `/api/connections/{id}/accept` | Accept a request sent to me |
| `DELETE` | `/api/connections/{id}` | Decline, cancel or remove |
| `GET` | `/actuator/health` | Health check |

## Authentication and sessions

- **Sessions, not tokens.** Login stores the user id in a server-side HTTP session. The browser only holds the `JSESSIONID` cookie, which is `HttpOnly` and `SameSite=Lax`, so page scripts can't read it. Sessions expire after 30 minutes idle (`SESSION_TIMEOUT`).
- **Passwords** are hashed with BCrypt. Login returns the same error for an unknown email and a wrong password.
- **Session fixation:** the session id changes on login. Logout invalidates the session and deletes the cookie.
- **CSRF:** every `POST`/`PUT`/`DELETE` must send the `XSRF-TOKEN` cookie value in an `X-XSRF-TOKEN` header. The frontend's HTTP client and Swagger UI do this automatically.
- Set `SESSION_COOKIE_SECURE=true` when serving over HTTPS.

The wiring is in `infrastructure/security/SecurityConfig.java`. Controllers get the signed-in user's id with `@AuthenticationPrincipal UUID userId`.
