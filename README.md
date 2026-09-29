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

## Running locally

There are two ways to run the app:

- **Option A: everything in Docker.** One command, and only Docker needs to be installed. Best for trying the app out.
- **Option B: development mode.** The database runs in Docker, while the backend and frontend run on your machine with live reload. Best for working on the code.

### 1. Setup

Install the tools for the option you'll use:

| Tool | Version | Needed for | Install |
|---|---|---|---|
| [Docker Desktop](https://www.docker.com/products/docker-desktop/) | any recent | A and B | macOS: `brew install --cask docker-desktop` |
| JDK | 21 | B (backend) | macOS: `brew install openjdk@21` |
| Maven | 3.9+ | B (backend) | macOS: `brew install maven` |
| Node.js | 20+ | B (frontend) | macOS: `brew install node@20`, or [nvm](https://github.com/nvm-sh/nvm) |

On Windows or Linux, install the same versions from [Adoptium](https://adoptium.net/) (JDK), [maven.apache.org](https://maven.apache.org/download.cgi) and [nodejs.org](https://nodejs.org/).

**Point Maven at JDK 21.** Homebrew's Maven uses the newest JDK by default, so add this to `~/.zshrc` (or `~/.bashrc`) and open a new terminal:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21   # Intel Macs: /usr/local/opt/openjdk@21
export PATH="$JAVA_HOME/bin:$PATH"
```

**Check the setup.** Start Docker Desktop first, then run:

```bash
docker info --format '{{.ServerVersion}}'   # prints a version, not "Cannot connect"
java -version                                 # 21.x
mvn -v                                        # shows "Java version: 21"
node -v                                       # v20 or newer
```

**Get the code:**

```bash
git clone <repository-url> DevNetworkApplication
cd DevNetworkApplication
```

**Configuration (optional).** The defaults work without any changes. Database credentials default to `devnetwork` / `devnetwork`. To change them, create a `.env` file in the repo root (git ignores it):

```bash
POSTGRES_DB=devnetwork
POSTGRES_USER=devnetwork
POSTGRES_PASSWORD=choose-a-password
```

In Option B, also give the backend the new values with `DB_USERNAME` and `DB_PASSWORD` (see [backend/README.md](backend/README.md#running-locally)).

### 2a. Option A: everything in Docker

```bash
docker compose up --build
```

The first build takes several minutes while Maven and npm download dependencies; later builds are much faster. The app is ready when the backend logs `Started DevNetworkApplication`.

| What | URL |
|---|---|
| App | http://localhost:3000 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health check | http://localhost:8080/actuator/health |

Press `Ctrl+C` to stop, or run `docker compose down` if you started it with `-d`.

### 2b. Option B: development mode

Use three terminals, all starting from the repo root.

**Terminal 1: database**

```bash
docker compose up -d database
```

**Terminal 2: backend** (http://localhost:8080)

```bash
cd backend
mvn spring-boot:run
```

On startup, Flyway creates or updates the database tables automatically.

**Terminal 3: frontend** (http://localhost:5173)

```bash
cd frontend
cp .env.example .env    # first time only
npm install             # first time, and after dependencies change
npm run dev
```

Open **http://localhost:5173**. The Vite dev server forwards `/api` requests to the backend, so both run on one origin and the session cookie just works. Code changes reload automatically: the frontend instantly, the backend after a restart (`Ctrl+C`, then `mvn spring-boot:run`).

> Don't run Option A and Option B at the same time: both use port 8080.

### 3. Try it out

1. Open the app and click **Create account**.
2. Fill in your profile on the **My account** page.
3. Open a private/incognito window, which gets its own session, and create a second account.
4. On **Members**, send a connection request from one account. Accept it from the other account's **Connections** page.

Swagger UI lets you call the API directly. Call `POST /api/auth/register` or `/api/auth/login` first; later requests then use that session.

### Running the tests

```bash
cd backend && mvn test          # unit tests, no database needed
cd frontend && npm run build    # type-check and production build
```

### Stopping and resetting

```bash
docker compose down        # stop containers, keep data
docker compose down -v     # stop containers and delete all database data
```

In Option B, stop the backend and frontend with `Ctrl+C` in their terminals.

### Troubleshooting

| Problem | Fix |
|---|---|
| `Cannot connect to the Docker daemon` | Start Docker Desktop and wait until it says it's running. |
| `port is already allocated` / `Address already in use` | Something else is using 5432, 8080, 3000 or 5173. Stop it (e.g. a local PostgreSQL: `brew services stop postgresql`), or find it with `lsof -i :8080`. |
| Maven fails with `release version 21 not supported`, or uses the wrong Java | `JAVA_HOME` isn't pointing at JDK 21. See **Point Maven at JDK 21** above; `mvn -v` should show Java 21. |
| Backend fails with `Connection refused` to `localhost:5432` | The database isn't running: `docker compose up -d database`. |
| Backend fails with a Flyway `validate` / checksum error | An already-applied migration was edited. For local data you can discard, reset with `docker compose down -v`. |
| Frontend shows "You need to sign in" or goes back to the login page | Sessions last 30 minutes idle and are lost when the backend restarts. Sign in again. |
| API returns `403 Forbidden` from curl or Postman | `POST`, `PUT` and `DELETE` requests need the CSRF header. See "Authentication and sessions" in [backend/README.md](backend/README.md#authentication-and-sessions). |

For more detail on each app, see [backend/README.md](backend/README.md), [frontend/README.md](frontend/README.md) and [database/README.md](database/README.md).
