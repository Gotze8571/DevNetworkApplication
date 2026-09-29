# DevNetwork — Fullstack Networking App

A developer networking app built with **React + TypeScript** (frontend), **Java + Spring Boot** (backend) and **PostgreSQL** (database).

```
.
├── frontend/            # React + TypeScript (Vite)
├── backend/             # Java 21 + Spring Boot 3
├── database/            # Database notes and conventions
├── docker-compose.yml   # Runs database + backend + frontend together
└── README.md
```

## Architecture

Both apps follow **Clean Architecture**: code is split into layers, and dependencies only point inward.

```
presentation ──► application ──► domain ◄── infrastructure
```

| Layer | Holds | May depend on |
|---|---|---|
| **domain** | Entities, business rules, repository interfaces (ports) | nothing |
| **application** | Use cases that orchestrate domain objects | domain |
| **infrastructure** | Implementations of ports: DB, HTTP clients, framework config | application, domain |
| **presentation** | REST controllers (backend) / React pages and components (frontend) | application, domain |

The domain layer never imports a framework, so business rules can be tested without Spring, React, or a database. See [backend/README.md](backend/README.md) and [frontend/README.md](frontend/README.md) for how each app maps these layers to folders.

## Features

- **Authentication:** register, sign in and sign out. Sessions are server-side, identified by an HttpOnly cookie, with CSRF protection. When a session expires, the frontend sends the user back to the sign-in page.
- **Accounts:** each user can view and edit their own display name and profile (headline, bio, location, links). Other members see a public version without the email.
- **Connections:** send, accept, decline, cancel and remove connection requests between members.

Each feature runs through every layer in both apps. Copy its shape when you add a new one.

## Quick start

Run everything with Docker:

```bash
docker compose up --build
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health check: http://localhost:8080/actuator/health

To run the apps locally for development, see each app's README.
